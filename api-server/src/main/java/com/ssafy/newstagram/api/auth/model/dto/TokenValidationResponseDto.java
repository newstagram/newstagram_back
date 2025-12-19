package com.ssafy.newstagram.api.auth.model.dto;

import lombok.Getter;

@Getter
public class TokenValidationResponseDto {
    private final String accessToken;
    private final String refreshToken;

    public TokenValidationResponseDto(String accessToken, String refreshToken){
        this.accessToken = "Bearer " + accessToken;
        this.refreshToken = refreshToken;
    }
}
