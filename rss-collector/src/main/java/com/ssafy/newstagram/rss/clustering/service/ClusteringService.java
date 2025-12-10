package com.ssafy.newstagram.rss.clustering.service;

import com.ssafy.newstagram.rss.clustering.util.EmbeddingLiteralUtil;
import com.ssafy.newstagram.rss.mapper.ArticleMapper;
import com.ssafy.newstagram.rss.vo.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusteringService {

    private final ArticleMapper articleMapper;
    private final DbscanService dbscanService;

    public void clustering() {
        // 1) 임베딩 값 포함한 모든 기사 조회 (id + embedding)
        List<Article> articles = articleMapper.findAllWithEmbeddingLiteral();

        // 2) 임베딩 double[] 변환
        double[][] embeddings = new double[articles.size()][];
        for (int i = 0; i < articles.size(); i++) {
            String literal = articleMapper.findEmbeddingLiteralById(articles.get(i).getId());
            embeddings[i] = EmbeddingLiteralUtil.fromLiteral(literal);
        }

        // 3) DBSCAN 클러스터링 수행
        double eps = 0.4;
        int minPts = 3;
        int[] labels = dbscanService.clustering(embeddings, eps, minPts);

    }

}
