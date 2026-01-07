package com.newstagram.api.logging.model.service;

import com.newstagram.api.logging.annotation.CollectLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogService {
    @CollectLog
    public void captureClickLog(Long articleId) {
        log.info("[Service] Kafka Log Article Click API - articleId={}", articleId);
    }
}
