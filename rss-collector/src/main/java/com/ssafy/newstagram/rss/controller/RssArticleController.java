package com.ssafy.newstagram.rss.controller;

import com.ssafy.newstagram.rss.dto.ArticleCollectResultDto;
import com.ssafy.newstagram.rss.service.RssArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rss")
@RequiredArgsConstructor
public class RssArticleController {

    private final RssArticleService rssArticleService;


    //전체 기사 불러오기
    @GetMapping("/getArticle")
    public ResponseEntity<ArticleCollectResultDto> collectAllArticles() {
        ArticleCollectResultDto result = rssArticleService.collectAllArticles();
        if(result.getErrors() == null || result.getErrors().isEmpty()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(result);
    }

     //특정 신문사 기사 불러오기
    @GetMapping("/getArticle/{sourceId}")
    public ResponseEntity<ArticleCollectResultDto> collectArticlesBySource(
            @PathVariable("sourceId") Long sourceId) {

        ArticleCollectResultDto result = rssArticleService.collectArticlesBySource(sourceId);
        if(result.getErrors() == null || result.getErrors().isEmpty()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(result);
    }



     // 특정 신문사 + 특정 카테고리 기사 불러오기
    @GetMapping("/getArticle/{sourceId}/{categoryId}")
    public ResponseEntity<ArticleCollectResultDto> collectArticlesBySourceAndCategory(
            @PathVariable("sourceId") Long sourceId,
            @PathVariable("categoryId") Long categoryId) {

        ArticleCollectResultDto result =
                rssArticleService.collectAllArticlesBySourceAndCategory(sourceId, categoryId);
        if(result.getErrors() == null || result.getErrors().isEmpty()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(result);
    }

    // DB에서 기사 전체 불러오기(가장 최근 100개만)

}
