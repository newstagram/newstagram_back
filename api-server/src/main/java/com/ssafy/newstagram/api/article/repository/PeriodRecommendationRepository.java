package com.ssafy.newstagram.api.article.repository;

import com.ssafy.newstagram.domain.recommend.entity.PeriodRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PeriodRecommendationRepository extends JpaRepository<PeriodRecommendation, Long> {

    // 특정 기간 단위 조회
    List<PeriodRecommendation> findByPeriodType(String periodType);

    // 기간 범위 조회
    List<PeriodRecommendation> findByPeriodTypeAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(
            String periodType, LocalDateTime start, LocalDateTime end);

    // 기간+랭킹 기준 조회
    List<PeriodRecommendation> findByPeriodTypeAndPeriodStartAndPeriodEndOrderByRankingAsc(
            String periodType, LocalDateTime start, LocalDateTime end);

    // 특정 기간 삭제
    void deleteByPeriodTypeAndPeriodStartAndPeriodEnd(String periodType, LocalDateTime start, LocalDateTime end);
}
