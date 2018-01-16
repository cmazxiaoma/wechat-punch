package com.cmazxiaoma.wx.core;

import com.cmazxiaoma.wx.constant.WxConfig;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenCache {

    public static Map<String, String> map = new HashMap<>();

    static  {
        map.put("access_token", WxConfig.ACCESS_TOKEN);
    }

    public static String get() {
        return map.get("access_token");
    }

    public static void update(String updatedToken) {
        map.put("access_token",updatedToken);
    }
}
