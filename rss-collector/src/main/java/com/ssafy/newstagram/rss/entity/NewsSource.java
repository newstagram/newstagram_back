package com.ssafy.newstagram.rss.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "RssNewsSource")
@Table(name = "news_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name="homepage_url")
    private String homepageUrl;
}
