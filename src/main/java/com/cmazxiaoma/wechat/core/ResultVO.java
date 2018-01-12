package com.cmazxiaoma.wechat.core;

import com.alibaba.fastjson.JSON;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 统一API响应结果封装
 */
@Getter
public class ResultVO {
    private final Integer code;
    private final String msg;
    private final Object data;

    public static class Builder {
        private int code;
        private String msg;
        private Object data;

        public Builder() {

        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResultVO build() {
            return new ResultVO(this);
        }
    }

    private ResultVO(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
