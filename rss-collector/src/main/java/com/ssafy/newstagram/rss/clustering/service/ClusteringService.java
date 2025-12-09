package com.ssafy.newstagram.rss.clustering.service;

import com.ssafy.newstagram.rss.clustering.util.EmbeddingLiteralUtil;
import com.ssafy.newstagram.rss.mapper.ArticleMapper;
import com.ssafy.newstagram.rss.vo.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 4) 클러스터별 index 그룹핑
        Map<Integer, List<Integer>> clusterGroups = groupByCluster(labels);

    }


    /**
     * 라벨 배열을 기반으로 클러스터별 인덱스 그룹핑
     */
    private Map<Integer, List<Integer>> groupByCluster(int[] labels) {
        Map<Integer, List<Integer>> clusterMap = new HashMap<>();

        for (int i = 0; i < labels.length; i++) {
            int label = labels[i];

            // noise는 스킵
            if (label == dbscanService.NOISE_INDEX) continue;

            clusterMap.computeIfAbsent(label, k -> new ArrayList<>()).add(i);
        }

        return clusterMap;
    }

}
