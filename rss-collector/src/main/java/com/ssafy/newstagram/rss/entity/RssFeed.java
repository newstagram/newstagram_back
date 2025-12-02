package com.ssafy.newstagram.rss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "RssRssFeed")
@Table(name = "rss_feeds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RssFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=255)
    private String name;

    @Column(name = "rss_url", nullable = false)
    private String rssUrl;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_fetched_at")
    private LocalDateTime lastFetchedAt;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "source_id", nullable = false)
    private Long sourceId;
}
