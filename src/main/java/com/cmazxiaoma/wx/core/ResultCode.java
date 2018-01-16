package com.cmazxiaoma.wx.core;

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
    INTERNAL_SERVER_ERROR(500, "internal_server_error"),

    //拒接访问
    FORBIDDEN(401, "您没有权限访问该资源"),

    //用户名或密码输入错误，登录失败
    USERNAME_PASSWORD_ERROR(1, "用户名或密码输入错误，登录失败"),

    //账号被禁用，请联系管理员
    ACCOUNT_CLOSED(2, "账号被禁用，请联系管理员"),

    //登录失败
    LOGIN_FAILED(3, "登录失败"),

    //非法参数
    ILLEGAL_PARAMETERS(4, "非法参数"),

    //查询无记录
    QUERY_NO_DATA(5, "查询无信息");

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
