package com.cmazxiaoma.wechat.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmazxiaoma.wechat.constant.WechatConfig;
import com.cmazxiaoma.wechat.core.AccessTokenCache;
import com.cmazxiaoma.wechat.core.JsapiTicketCache;
import com.cmazxiaoma.wechat.core.ResultVO;
import com.cmazxiaoma.wechat.core.ResultVOGenerator;
import com.cmazxiaoma.wechat.http.HttpAPIService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxJSTokenUtil {

    private HttpAPIService httpAPIService;

    private WxJSTokenUtil() {
        httpAPIService = SpringContextUtil.getBean(HttpAPIService.class);
    }

    public static WxJSTokenUtil getInstance() {
        return WxJSTokenUtilHolder.wxJsTokenUtil;
    }

    private static class WxJSTokenUtilHolder {
        private static final WxJSTokenUtil wxJsTokenUtil = new WxJSTokenUtil();
    }

    public ResultVO genJSSignture(String url) {
        String content = null;
        String errcode = null;
        String jsapi_ticket = null;
        JSONObject jsonObject = null;
        do {
            String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                    + AccessTokenCache.get()
                    + "&type=jsapi";

            content = httpAPIService.doGet(jsapi_ticket_url).getBody();
            jsonObject = JSON.parseObject(content);
            errcode = jsonObject.getString("errcode");

            if ("42001".equalsIgnoreCase(errcode)) {
                WxHttpUtil.getInstance().updateAccessToken();
            }
        } while ("42001".equalsIgnoreCase(errcode));

        if (errcode.equalsIgnoreCase("0")) {
            jsapi_ticket = JSON.parseObject(content).getString("ticket");
            log.info("jsapi_ticket = {}", jsapi_ticket);
            JsapiTicketCache.update(jsapi_ticket);
        } else {
            return ResultVOGenerator.genFailResult("请求失败jsapi_ticket");
        }

        String nonceStr = "cmazxiaoma";
        String timestamp = "20141012";

        String str = "jsapi_ticket=" + jsapi_ticket
                + "&noncestr=" + nonceStr
                + "&timestamp=" + timestamp
                + "&url=" + url;

        String signature = WxCheckUtil.getSha1(str);
        log.info("signature = {}", signature);
        Map<String, String> map = new HashMap<>();
        map.put("appId", WechatConfig.APP_ID);
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);

        return ResultVOGenerator.genSuccessResult(map);
    }
}
