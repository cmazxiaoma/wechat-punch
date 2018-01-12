package com.cmazxiaoma.wechat.core;

/**
 * 响应结果生成工具
 */
public class ResultVOGenerator {

    public static ResultVO genSuccessResult() {
        return new ResultVO.Builder()
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMsg())
                .build();
    }

    public static ResultVO genSuccessResult(Object data) {
        return new ResultVO.Builder()
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMsg())
                .data(data)
                .build();
    }

    public static ResultVO genFailResult(String message) {
        return new ResultVO.Builder()
                .code(ResultCode.FAIL.getCode())
                .msg(message)
                .build();
    }
}
