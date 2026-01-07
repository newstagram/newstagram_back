package com.newstagram.rss.repository;

import com.newstagram.rss.entity.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
}
