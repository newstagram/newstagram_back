package com.ssafy.newstagram.rss.batch;


import com.ssafy.newstagram.rss.entity.NewsSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import com.ssafy.newstagram.rss.repository.NewsSourceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class NewsSourceItemReader implements ItemReader<Long> {
    private final NewsSourceRepository newsSourceRepository;

    private List<Long> sourceIds;
    private int nextIndex = 0;

    @Override
    public Long read(){
        if(sourceIds == null){
            List<NewsSource> sources = newsSourceRepository.findAll();
            sourceIds = sources.stream()
                    .map(NewsSource::getId)
                    .collect(Collectors.toList());
        }

        if(nextIndex < sourceIds.size()){
            return sourceIds.get(nextIndex++);
        }

        return null;
    }
}
