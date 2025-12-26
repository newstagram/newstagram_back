package com.ssafy.newstagram.api.article.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.newstagram.api.article.dto.ArticleDto;
import com.ssafy.newstagram.api.article.dto.EmbeddingResponse;
import com.ssafy.newstagram.api.article.dto.SearchHistoryDto;
import com.ssafy.newstagram.api.article.repository.NewsCategoryRepository;
import com.ssafy.newstagram.api.article.repository.ArticleRepository;
import com.ssafy.newstagram.api.users.repository.UserRepository;
import com.ssafy.newstagram.api.users.repository.UserSearchHistoryRepository;
import com.ssafy.newstagram.domain.news.entity.Article;
import com.ssafy.newstagram.domain.user.entity.User;
import com.ssafy.newstagram.domain.user.entity.UserSearchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ArticleRepository articleRepository;
    private final UserSearchHistoryRepository userSearchHistoryRepository;
    private final UserRepository userRepository;
    private final NewsCategoryRepository newsCategoryRepository;

    @Autowired
    @Lazy
    private SearchService self;

    @Value("${gms.api.base-url}")
    private String gmsApiBaseUrl;
    @Value("${gms.api.llm-base-url}")
    private String gmsLlmBaseUrl;
    @Value("${gms.api.key}")
    private String gmsApiKey;

    private static final String MODEL_NAME = "text-embedding-3-large";
    private static final String LLM_MODEL_NAME = "gpt-4o-mini";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Transactional(readOnly = true)
    public List<ArticleDto> searchArticles(Long userId, String query, int limit, int page) {
        if (page == 0) {
            self.saveSearchHistory(userId, query);
        }
        return self.getCachedSearchResults(query, limit, page, 0.80);
    }

    @Cacheable(value = "search_results", key = "#query + '-' + #page + '-' + #limit + '-' + #threshold")
    public List<ArticleDto> getCachedSearchResults(String query, int limit, int page, double threshold) {
        long totalStartTime = System.currentTimeMillis();
        log.info("[Search] Original Query: {}, Page: {}", query, page);

        // 1. LLM Analysis
        SearchAnalysisResult analysis = analyzeQueryWithLLM(query);
        log.info("[Search] LLM Analysis Result: {}", analysis);

        // 2. Prepare for DB Query
        final LocalDateTime startDate = (analysis.getDateRange() > 0)
                ? LocalDateTime.now().minusDays(analysis.getDateRange())
                : LocalDateTime.now().minusDays(7);

        List<Long> categoryIds = new ArrayList<>();
        if (analysis.getCategories() != null) {
            for (String code : analysis.getCategories()) {
                newsCategoryRepository.findByName(code).ifPresent(category -> categoryIds.add(category.getId()));
            }
        }

        List<String> keywordsForSearch = (analysis.getSearchKeywords() != null && !analysis.getSearchKeywords().isEmpty())
            ? analysis.getSearchKeywords()
            : List.of(query);

        // 여러 키워드 임베딩을 가져와 평균을 계산
        List<List<Double>> embeddings = keywordsForSearch.stream()
                                            .map(self::getCachedEmbedding)
                                            .collect(Collectors.toList());

        List<Double> averageEmbedding = calculateAverageEmbedding(embeddings);
        String embeddingString = toPgVectorLiteral(averageEmbedding);
        // --- [로직 수정 끝] ---

        int candidateLimit = 800;
        
        // DB 쿼리는 단 한번만 호출
        List<Article> candidateArticles = articleRepository.findCandidatesByEmbedding(
                embeddingString, candidateLimit, categoryIds, startDate, threshold);
        log.info("[Search] Found {} candidate articles from vector search.", candidateArticles.size());

        // 4. 키워드 필터링은 'primary_keywords'를 사용
        List<String> keywordsForFilter = (analysis.getPrimaryKeywords() != null && !analysis.getPrimaryKeywords().isEmpty())
            ? analysis.getPrimaryKeywords()
            : keywordsForSearch; // primary_keywords가 없으면 search_keywords를 대신 사용 (Fallback)

        List<Article> filteredArticles = candidateArticles.stream()
                .filter(article -> {
                    String title = article.getTitle() != null ? article.getTitle().toLowerCase() : "";
                    String description = article.getDescription() != null ? article.getDescription().toLowerCase() : "";
                    // 'keywordsForFilter' 중 하나라도 포함되면 통과
                    return keywordsForFilter.stream()
                            .anyMatch(keyword ->
                                    title.contains(keyword.toLowerCase()) || description.contains(keyword.toLowerCase())
                            );
                })
                .collect(Collectors.toList());
        log.info("[Search] {} articles remaining after PRIMARY keyword filtering.", filteredArticles.size());

        // 5. 최종 정렬 및 메모리 기반 페이징
        List<Article> sortedArticles = filteredArticles.stream()
                .sorted(Comparator.comparing(Article::getPublishedAt).reversed()) // 최신순으로 정렬
                .collect(Collectors.toList());

        int startIdx = page * limit;
        if (startIdx >= sortedArticles.size()) {
            return new ArrayList<>();
        }
        int endIdx = Math.min(startIdx + limit, sortedArticles.size());

        List<ArticleDto> result = sortedArticles.subList(startIdx, endIdx).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        long totalEndTime = System.currentTimeMillis();
        log.info("[Search] Total Service Execution took {} ms, query : {}", (totalEndTime - totalStartTime), query);

        return result;
    }

    // 평균 임베딩을 계산하는 유틸리티 메서드 (클래스 내부에 추가)
    private List<Double> calculateAverageEmbedding(List<List<Double>> embeddings) {
        if (embeddings == null || embeddings.isEmpty()) {
            return new ArrayList<>();
        }
        // 모든 벡터가 동일한 차원을 가지고 있다고 가정 (e.g., 1536)
        int dimensions = embeddings.get(0).size();
        List<Double> averageVector = new ArrayList<>(Collections.nCopies(dimensions, 0.0));

        for (List<Double> vector : embeddings) {
            for (int i = 0; i < dimensions; i++) {
                averageVector.set(i, averageVector.get(i) + vector.get(i));
            }
        }

        for (int i = 0; i < dimensions; i++) {
            averageVector.set(i, averageVector.get(i) / embeddings.size());
        }
        return averageVector;
    }

    // Article ID 기준으로 중복을 제거하기 위한 유틸리티 메서드
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class SearchAnalysisResult {
        // @JsonProperty는 LLM이 보내주는 JSON의 key와 필드명을 매핑합니다.
        @com.fasterxml.jackson.annotation.JsonProperty("primary_keywords")
        private List<String> primaryKeywords;

        @com.fasterxml.jackson.annotation.JsonProperty("search_keywords")
        private List<String> searchKeywords;

        private int dateRange;
        private List<String> categories;
    }

    private SearchAnalysisResult analyzeQueryWithLLM(String query) {
        if (query == null || query.isBlank()) {
            return new SearchAnalysisResult(new ArrayList<>(), new ArrayList<>(), 7, new ArrayList<>());
        }

        String prompt = """
        You are an expert search query pre-processor for a news article system.
        Your task is to analyze a user's natural language query and convert it into a structured JSON object.

        Analyze the query and extract the following information in JSON format:

        1.  'primary_keywords': 1 or 2 word of essential, core nouns/entities from the original query. These are "must-include" keywords for final filtering.
        2.  'search_keywords': A list of at least 3 semantically rich keywords for vector embedding search. This list MUST include all 'primary_keywords' and also expand with synonyms and related concepts.
        3.  'dateRange': An integer representing the lookback period in days. (Rules are the same as before).
        4.  'categories': A list of up to 3 relevant category codes. (List is the same as before).

        Categories List:
        TOP, POLITICS, ECONOMY, BUSINESS, SOCIETY, LOCAL, WORLD, NORTH_KOREA, CULTURE_LIFE, ENTERTAINMENT, SPORTS, WEATHER, SCIENCE_ENV, HEALTH, OPINION, PEOPLE

        ---
        Here are some high-quality examples:

        Query: "페이커 뉴스"
        JSON Output:
        {
        "primary_keywords": ["페이커"],
        "search_keywords": ["페이커", "e스포츠", "리그 오브 레전드", "선수 소식"],
        "dateRange": 7,
        "categories": ["SPORTS", "PEOPLE"]
        }

        Query: "삼성전자 최근 주가 흐름 알려줘"
        JSON Output:
        {
        "primary_keywords": ["삼성전자"],
        "search_keywords": ["삼성전자", "주가", "주식", "시세"],
        "dateRange": 7,
        "categories": ["ECONOMY", "BUSINESS"]
        }

        Query: "오늘 가장 핫한 뉴스가 뭐야?"
        JSON Output:
        {
        "primary_keywords": ["헤드라인", "속보"],
        "search_keywords": ["주요 뉴스", "헤드라인", "속보", "오늘의 이슈"],
        "dateRange": 1,
        "categories": ["TOP", "SOCIETY"]
        }
        ---

        Now, analyze the following query and provide the JSON output.

        Query: 
        """ + query;
        
        String url = gmsApiBaseUrl.endsWith("/")
                ? gmsApiBaseUrl + "chat/completions"
                : gmsApiBaseUrl + "/chat/completions";

        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(java.util.Map.of(
                    "model", LLM_MODEL_NAME,
                    "messages", List.of(
                            java.util.Map.of("role", "developer", "content", "You are a helpful assistant that analyzes news search queries and returns JSON."),
                            java.util.Map.of("role", "user", "content", prompt)
                    ),
                    "response_format", java.util.Map.of("type", "json_object")
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(gmsApiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<com.fasterxml.jackson.databind.JsonNode> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, com.fasterxml.jackson.databind.JsonNode.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String content = response.getBody().path("choices").get(0).path("message").path("content").asText();
                log.info("[Search] LLM Response: {}", content);
                return OBJECT_MAPPER.readValue(content, SearchAnalysisResult.class);
            }
        } catch (Exception e) {
            log.error("[Search] LLM Analysis Failed", e);
        }

        List<String> fallbackKeywords = Arrays.asList(query.split("\\s+"));
        return new SearchAnalysisResult(fallbackKeywords, fallbackKeywords, 7, new ArrayList<>());
    }

    private String toPgVectorLiteral(List<Double> embedding){
        if (embedding == null) return "[]";
        String inner = embedding.stream()
                .map(d -> String.format(java.util.Locale.US, "%.6f", d))
                .collect(Collectors.joining(","));
        return "[" + inner + "]";
    }

    @Cacheable(value = "keyword_embedding", key = "#inputText")
    public List<Double> getCachedEmbedding(String inputText) {
        if (inputText == null || inputText.isBlank()) {
            throw new IllegalArgumentException("Embedding input text must not be empty");
        }

        String url = gmsApiBaseUrl.endsWith("/")
                ? gmsApiBaseUrl + "embeddings"
                : gmsApiBaseUrl + "/embeddings";
        
        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(Map.of(
                "model", MODEL_NAME,
                "input", inputText,
                "dimensions",1536
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(gmsApiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<EmbeddingResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, EmbeddingResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                EmbeddingResponse body = response.getBody();
                if (body.getData() != null && !body.getData().isEmpty()) {
                    return body.getData().get(0).getEmbedding();
                }
            }
            log.error("[Embedding] API 응답 실패 또는 데이터 없음, status={}", response.getStatusCode());
            throw new RuntimeException("Embedding API 응답 데이터 비어있음");

        } catch (HttpClientErrorException e) {
            log.error("[Embedding] GMS 4xx 에러. status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("GMS/OpenAI 4xx 에러: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("[Embedding] GMS 통신 또는 직렬화 실패", e);
            throw new RuntimeException("Embedding API 호출 실패", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchHistory(Long userId, String query) {
        try {
            int updated = userSearchHistoryRepository.updateCreatedAtByUserIdAndQuery(userId, query);
            if (updated == 0) {
                User user = userRepository.getReferenceById(userId);
                UserSearchHistory history = UserSearchHistory.builder()
                        .user(user)
                        .query(query)
                        .build();
                userSearchHistoryRepository.save(history);
            }
        } catch (Exception e) {
            log.error("[Search History] Failed to save search history for user={}", userId, e);
        }
    }


    private ArticleDto convertToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .description(article.getDescription())
                .url(article.getUrl())
                .thumbnailUrl(article.getThumbnailUrl())
                .author(article.getAuthor())
                .publishedAt(article.getPublishedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> getRecommendedArticles(Long userId, int page, int limit) {
        // Use native query to fetch embedding as string to avoid Hibernate mapping issues
        String embeddingStr = userRepository.findPreferenceEmbeddingAsString(userId);
        
        if (embeddingStr == null || embeddingStr.isBlank()) {
            return new ArrayList<>();
        }

        // Fix time range to 7 days
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        int offset = page * limit;

        // Use a relaxed threshold (2.0) for recommendations to allow infinite scrolling
        List<Article> articles = articleRepository.findByEmbeddingSimilarityWithFilters(
                embeddingStr, limit, offset, null, startDate, 2.0);

        return articles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SearchHistoryDto> getSearchHistory(Long userId) {
        return userSearchHistoryRepository.findHistoryNative(userId).stream()
                .map(history -> new SearchHistoryDto(history.getId(), history.getQuery()))
                .limit(20)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSearchHistory(Long userId, Long historyId) {
        int deletedCount = userSearchHistoryRepository.deleteByIdAndUserId(historyId, userId);
        if (deletedCount == 0) {
            throw new IllegalArgumentException("History not found or unauthorized");
        }
    }

    @Transactional
    public void updateSearchHistory(Long userId, Long historyId, String newQuery) {
        int updatedCount = userSearchHistoryRepository.updateQueryByIdAndUserId(historyId, userId, newQuery);
        if (updatedCount == 0) {
            throw new IllegalArgumentException("History not found or unauthorized");
        }
    }
}
