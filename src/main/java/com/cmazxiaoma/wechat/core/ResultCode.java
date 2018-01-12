package com.cmazxiaoma.wechat.core;

import lombok.Data;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */

public enum ResultCode {
    //成功
    SUCCESS(200, "success"),

    //失败
    FAIL(400, "fail"),

    //未认证（签名错误）
    UNAUTHORIZED(401, "unauthorized"),

    //接口不存在
    NOT_FOUND(404, "not found"),

    //服务器内部错误
    INTERNAL_SERVER_ERROR(500, "internal_server_error");

    private Integer code;

    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
