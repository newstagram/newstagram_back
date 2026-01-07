package com.newstagram.api.exception;

public class TokenException extends RuntimeException{
    public TokenException(String message) {
        super(message);
    }
}
