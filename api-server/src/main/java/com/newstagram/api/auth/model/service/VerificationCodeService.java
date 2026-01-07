package com.newstagram.api.auth.model.service;

import com.newstagram.api.auth.model.dto.EmailFindRequestDto;
import com.newstagram.api.auth.model.dto.EmailFindVerifyRequestDto;
import com.newstagram.api.auth.model.dto.PhoneVerificationConfirmDto;
import com.newstagram.api.auth.model.dto.PhoneVerificationRequestDto;

public interface VerificationCodeService {
    void requestEmailFindVerificationCode(EmailFindRequestDto dto, long expirationMs);
    String verifyAndGetEmail(EmailFindVerifyRequestDto dto);

    void requestPhoneVerificationCode(PhoneVerificationRequestDto dto, long expirationMs);
    void confirmPhoneVerification(PhoneVerificationConfirmDto dto);
    boolean checkVerified(String phoneNumber);
    void deletePhoneVerificationKey(String phoneNumber);
}
