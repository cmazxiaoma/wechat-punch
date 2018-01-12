package com.cmazxiaoma.wechat.core;

import com.cmazxiaoma.wechat.constant.WechatConfig;

import java.util.HashMap;
import java.util.Map;

public class JsapiTicketCache {

    public static Map<String, String> map = new HashMap<>();

    static  {
        map.put("jsapi_tikcet", WechatConfig.JSAPI_TICKET);
    }

    public static String get() {
        return map.get("jsapi_tikcet");
    }

    public static void update(String updatedToken) {
        map.put("jsapi_tikcet",updatedToken);
    }

}
