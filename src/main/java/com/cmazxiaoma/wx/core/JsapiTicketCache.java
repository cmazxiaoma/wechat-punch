package com.cmazxiaoma.wx.core;

import com.cmazxiaoma.wx.constant.WxConfig;

import java.util.HashMap;
import java.util.Map;

public class JsapiTicketCache {

    public static Map<String, String> map = new HashMap<>();

    static  {
        map.put("jsapi_tikcet", WxConfig.JSAPI_TICKET);
    }

    public static String get() {
        return map.get("jsapi_tikcet");
    }

    public static void update(String updatedToken) {
        map.put("jsapi_tikcet",updatedToken);
    }

}
