package com.ssafy.newstagram.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    private ErrorDetail error;

    public static <T> BaseResponse<T> success(String code, String message, T result) {
        return new BaseResponse<>(true, code, message, result, null);
    }

    public static BaseResponse<?> successNoData(String code) {
        return new BaseResponse<>(true, code, "요청에 성공하였습니다.", null, null);
    }

    public static BaseResponse<?> successNoData(String code, String message) {
        return new BaseResponse<>(true, code, message, null, null);
    }

    public static BaseResponse<?> error(String code, String message, ErrorDetail error) {
        return new BaseResponse<>(false, code, message, null, error);
    }
}
