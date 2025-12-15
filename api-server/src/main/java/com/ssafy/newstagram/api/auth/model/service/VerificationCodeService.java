package com.ssafy.newstagram.api.auth.model.service;

import com.ssafy.newstagram.api.auth.model.dto.EmailFindRequestDto;
import com.ssafy.newstagram.api.auth.model.dto.EmailFindVerifyRequestDto;

public interface VerificationCodeService {
    void requestVerificationCode(EmailFindRequestDto dto, long expirationMs);

    String verifyAndGetEmail(EmailFindVerifyRequestDto dto);

}
