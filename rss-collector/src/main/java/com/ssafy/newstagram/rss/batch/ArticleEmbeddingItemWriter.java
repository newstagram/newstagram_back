package com.ssafy.newstagram.rss.batch;

import com.ssafy.newstagram.rss.service.ArticleVectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j
public class ArticleEmbeddingItemWriter implements ItemWriter<Long> {

    private final ArticleVectorService articleVectorService;

    @Override
    public void write(Chunk<? extends Long> items) {
        for (Long sourceId : items) {
            if (sourceId == null) {
                continue;
            }

            log.info("[EmbeddingStep] 시작 - sourceId={}", sourceId);

            ArticleVectorService.VectorizeResult result =
                    articleVectorService.vectorizeForSource(sourceId);

            log.info(
                    "[EmbeddingStep] 완료 - sourceId={}, total={}, success={}, hasGmsError={}, status={}",
                    result.getSourceId(),
                    result.getTotalCount(),
                    result.getSuccessCount(),
                    result.isHasGmsError(),
                    result.getStatus()
            );
        }
    }
}

