package com.cmazxiaoma.wechat.exception;

public class AccessTokenExpiredException extends  RuntimeException {

    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
