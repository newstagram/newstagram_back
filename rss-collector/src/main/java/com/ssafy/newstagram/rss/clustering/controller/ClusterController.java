package com.ssafy.newstagram.rss.clustering.controller;

import com.ssafy.newstagram.rss.clustering.service.ClusteringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClusterController {

    private final ClusteringService clusteringService;

    @GetMapping("/clustering")
    public String clustering() {
        clusteringService.clustering();
        return "클러스터링 완료";
    }

}
