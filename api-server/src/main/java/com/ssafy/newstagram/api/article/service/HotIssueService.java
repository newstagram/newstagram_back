package com.ssafy.newstagram.api.article.service;

import com.ssafy.newstagram.api.article.dto.HotIssueArticleDto;
import com.ssafy.newstagram.api.article.dto.HotIssueGroupDto;
import com.ssafy.newstagram.api.article.dto.HotIssueItemDto;
import com.ssafy.newstagram.api.article.dto.HotIssueSetDto;
import com.ssafy.newstagram.api.article.repository.PeriodRecommendationRepository;
import com.ssafy.newstagram.domain.util.period.Period;
import com.ssafy.newstagram.domain.util.period.PeriodCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotIssueService {

    private final PeriodRecommendationRepository periodRecommendationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public HotIssueSetDto getPeriodRecommendationsTopNPerRankingByPeriodType(
            String periodType,
            int limitCount
    ) {
        Period period = Period.valueOf(periodType);
        PeriodCalculator periodCalculator = period.getCalculator();

        // =====================
        // 1️⃣ 현재 period
        // =====================
        LocalDateTime periodStart = periodCalculator.getStart();
        String periodKey = buildPeriodKey(period.name(), periodStart);

        // =====================
        // 2️⃣ Redis LOOK-ASIDE
        // =====================
        HotIssueSetDto cached =
                (HotIssueSetDto) redisTemplate.opsForValue().get(periodKey);

        if (cached != null) {
            log.info("[REDIS] cache HIT - key={}", periodKey);
            return cached;
        }

        log.info("[REDIS] cache MISS - key={}", periodKey);

        // =====================
        // 3️⃣ DB 조회
        // =====================
        List<HotIssueItemDto> results =
                periodRecommendationRepository.findTopNPerRanking(
                        periodType,
                        periodStart,
                        limitCount
                );

        // =====================
        // 4️⃣ 결과 없으면 이전 period
        // =====================
        if (results.isEmpty()) {
            log.info("현재 period 결과 없음 → 이전 period 조회");

            periodStart = periodCalculator.getBeforeStart();
            periodKey = buildPeriodKey(period.name(), periodStart);

            cached = (HotIssueSetDto) redisTemplate.opsForValue().get(periodKey);
            if (cached != null) {
                log.info("[REDIS] cache HIT - key={}", periodKey);
                return cached;
            }

            log.info("[REDIS] cache MISS - key={}", periodKey);

            results =
                    periodRecommendationRepository.findTopNPerRanking(
                            periodType,
                            periodStart,
                            limitCount
                    );

            if (results.isEmpty()) {
                log.info("이전 period 결과도 없음");

                HotIssueSetDto empty = HotIssueSetDto.builder()
                        .periodKey(periodKey)
                        .periodType(periodType)
                        .groups(List.of())
                        .build();

                redisTemplate.opsForValue().set(periodKey, empty);
                return empty;
            }
        }

        // =====================
        // 5️⃣ DTO 변환 (🔥 핵심)
        // =====================
        HotIssueSetDto response =
                toHotIssueSetDto(periodKey, periodType, results);

        // =====================
        // 6️⃣ Redis 저장
        // =====================
        redisTemplate.opsForValue().set(periodKey, response);

        log.info("[REDIS] cache SET - key={}, groupSize={}",
                periodKey, response.getGroups().size());

        return response;
    }

    // =====================
    // 캐시 키 생성
    // =====================
    private String buildPeriodKey(String periodType, LocalDateTime periodStart) {
        return String.format(
                "hot-issue:%s:%s",
                periodType,
                periodStart.format(DateTimeFormatter.ofPattern("yyyyMMddHH"))
        );
    }

    // =====================
    // DB 결과 → Redis DTO 변환
    // =====================
    private HotIssueSetDto toHotIssueSetDto(
            String periodKey,
            String periodType,
            List<HotIssueItemDto> results
    ) {
        Map<Integer, List<HotIssueItemDto>> grouped =
                results.stream()
                        .collect(Collectors.groupingBy(
                                HotIssueItemDto::getGroupRanking,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        List<HotIssueGroupDto> groups = grouped.entrySet().stream()
                .map(entry -> {
                    int groupRanking = entry.getKey();
                    List<HotIssueItemDto> groupItems = entry.getValue();

                    List<HotIssueArticleDto> articles = groupItems.stream()
                            .sorted(Comparator.comparingInt(HotIssueItemDto::getRankInGroup))
                            .map(item -> HotIssueArticleDto.builder()
                                    .articleId(item.getArticleId())
                                    .rankInGroup(item.getRankInGroup())
                                    .build())
                            .toList();

                    return HotIssueGroupDto.builder()
                            .groupRanking(groupRanking)
                            .articles(articles)
                            .build();
                })
                .toList();

        return HotIssueSetDto.builder()
                .periodKey(periodKey)
                .periodType(periodType)
                .groups(groups)
                .build();
    }
}
