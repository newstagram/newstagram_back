package com.newstagram.logging.domain;

import com.newstagram.logging.global.util.DoubleListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;

@Entity(name = "LoggingArticle")
@Table(name = "articles")
@Getter
public class Article {
    @Id
    private Long id;

    @Column(name = "embedding", columnDefinition = "vector")
    @Convert(converter = DoubleListConverter.class)
    private List<Double> embedding;
}