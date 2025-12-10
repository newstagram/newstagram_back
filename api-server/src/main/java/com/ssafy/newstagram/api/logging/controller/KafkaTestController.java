package com.ssafy.newstagram.api.logging.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.newstagram.api.logging.dto.UserInteractionLogsDto;
import com.ssafy.newstagram.api.logging.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KafkaTestController {

    private final KafkaProducerService producerService;
    private final ObjectMapper objectMapper;

    // Postman에서 이 주소를 칠 겁니다.
    @PostMapping("/test/click-log")
    public String sendTestLog(@RequestParam Long userId, @RequestParam Long articleId) throws JsonProcessingException {
        // 1. 강제로 DTO 만들기 (로그인 안 해도 됨)
        UserInteractionLogsDto logDto = UserInteractionLogsDto.builder()
                .userId(userId)        // 파라미터로 받은 유저 ID
                .articleId(articleId)  // 파라미터로 받은 기사 ID
                .interactionType("CLICK")
                .createdAt(LocalDateTime.now())
                .sessionId("test-session")
                .ipAddress("127.0.0.1")
                .userAgent("Postman-Test-Agent")
                .build();

        // 2. Kafka로 전송 (Aspect가 하는 일을 흉내냄)
        String jsonMessage = objectMapper.writeValueAsString(logDto);
        producerService.sendMessage("user.log.article.click", jsonMessage);

        return "userId=" + userId + ", articleId=" + articleId;
    }

    @GetMapping("/test/hello")
    public String test() {
        return "init!! Hello";
    }
}