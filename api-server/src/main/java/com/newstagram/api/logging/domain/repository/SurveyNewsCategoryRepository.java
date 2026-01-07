package com.newstagram.api.logging.domain.repository;

import com.newstagram.domain.news.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyNewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
    List<NewsCategory> findAllByIdNotIn(List<Long> ids);
}
