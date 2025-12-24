package com.ssafy.newstagram.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String type; // 에러 유형
    private String code; // 구체적인 에러 코드
    private String message;  // 구체적 에러 메시지
    private Object metadata; // 상황에 따라 자유롭게 넣고 싶은 추가 데이터
}
