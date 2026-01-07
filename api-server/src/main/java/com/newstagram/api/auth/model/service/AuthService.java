package com.newstagram.api.auth.model.service;

import com.newstagram.api.auth.model.dto.TokenValidationRequestDto;
import com.newstagram.api.auth.model.dto.TokenValidationResponseDto;

import com.newstagram.api.auth.model.dto.PasswordResetRequestDto;
import com.newstagram.api.auth.model.dto.PasswordResetRequestRequestDto;

public interface AuthService {
    void logout(Long userId);
    void requestPasswordReset(PasswordResetRequestRequestDto dto);
    void passwordReset(PasswordResetRequestDto dto);
    TokenValidationResponseDto validateToken(TokenValidationRequestDto dto);
}
