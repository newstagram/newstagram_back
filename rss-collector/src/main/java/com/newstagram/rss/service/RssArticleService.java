package com.newstagram.rss.service;

import com.newstagram.rss.dto.ArticleCollectResultDto;


public interface RssArticleService {
    ArticleCollectResultDto collectAllArticles();
    ArticleCollectResultDto collectArticlesBySource(Long sourceId);
    ArticleCollectResultDto collectAllArticlesBySourceAndCategory(Long sourceId, Long categoryId);

    ArticleCollectResultDto collectArticlesByFeed(Long feedId);
}
