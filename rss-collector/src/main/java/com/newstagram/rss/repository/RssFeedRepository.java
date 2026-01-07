package com.newstagram.rss.repository;

import com.newstagram.rss.entity.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RssFeedRepository extends JpaRepository<RssFeed, Long> {
        List<RssFeed> findBySourceIdAndIsActiveTrue(Long sourceId);
}
