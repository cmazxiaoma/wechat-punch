package com.cmazxiaoma.wechat.core;

import com.cmazxiaoma.wechat.constant.WechatConfig;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenCache {

    public static Map<String, String> map = new HashMap<>();

    static  {
        map.put("access_token", WechatConfig.ACCESS_TOKEN);
    }

    public static String get() {
        return map.get("access_token");
    }

    public static void update(String updatedToken) {
        map.put("access_token",updatedToken);
    }
}
