package com.newstagram.api.auth.model.dto;

import com.newstagram.api.users.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class EmailFindRequestDto {
    @Schema(description = "휴대폰번호", example = "01012345678")
    @ValidPhoneNumber
    private String phoneNumber;
}
