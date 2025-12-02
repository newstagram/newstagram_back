package com.ssafy.newstagram.rss.repository;

import com.ssafy.newstagram.rss.entity.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
}
