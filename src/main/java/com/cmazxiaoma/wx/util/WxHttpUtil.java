package com.cmazxiaoma.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmazxiaoma.wx.VO.WxQRTicketVO;
import com.cmazxiaoma.wx.VO.WxUserListVO;
import com.cmazxiaoma.wx.constant.WxConfig;
import com.cmazxiaoma.wx.core.AccessTokenCache;
import com.cmazxiaoma.wx.http.HttpAPIService;
import com.cmazxiaoma.wx.http.HttpResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxHttpUtil {

    private HttpAPIService httpAPIService;

    private WxHttpUtil() {
        httpAPIService = SpringContextUtil.getBean(HttpAPIService.class);
    }

    public static WxHttpUtil getInstance() {
        return WechatHttpUtilHolder.wechatHttpUtil;
    }

    private static class WechatHttpUtilHolder {
        private static final WxHttpUtil wechatHttpUtil = new WxHttpUtil();
    }

    //回复a，查询该公众号关注者列表
    public String queryWechatUserList() {
        String content = null;
        String errcode = null;

        do {
            String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="
                    + AccessTokenCache.get();
            content = httpAPIService.doGet(url).getBody();
            errcode = JSON.parseObject(content).getString("errcode");

            if ("42001".equalsIgnoreCase(errcode)) {
                updateAccessToken();
            }
        } while ("42001".equalsIgnoreCase(errcode));

        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="
                + AccessTokenCache.get();
        content = httpAPIService.doGet(url).getBody();
        WxUserListVO userListVO = JSON.parseObject(content, new TypeReference<WxUserListVO>(){});
        log.info("userListVO = {}", userListVO);

        return userListVO.toString();
    }

    public String queryCurrentUserInfo(String userOpenId) {
        String content = null;
        String errcode = null;
        do {

            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
                    + AccessTokenCache.get() +
                    "&openid="
                    + userOpenId
                    +"&lang=zh_CN";
            content = httpAPIService.doGet(url).getBody();
            errcode = JSON.parseObject(content).getString("errcode");

            if ("42001".equalsIgnoreCase(errcode)) {
                updateAccessToken();
            }

        } while ("42001".equalsIgnoreCase(errcode));

        log.info("content = {}", content);
        return content;
    }

    //获取QRCode图片MediaId
    public String getQRCodeMediaId() {
        return WxFileUtil.upload(WxConfig.QRCode, "image");
    }

    //实时获取QRCode图片
    public String getQRCodeUrl() {
        String content = null;
        String errcode = null;
        do {
            String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
                    + AccessTokenCache.get();
            String jsonData = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
            content = httpAPIService.doPost(url, jsonData).getBody();
            errcode = JSON.parseObject(content).getString("errcode");
            log.info("content = {}", content);

            if ("42001".equalsIgnoreCase(errcode)) {
                updateAccessToken();
            }

        } while ("42001".equalsIgnoreCase(errcode));

        WxQRTicketVO wechatQRTicketVO = JSON.parseObject(content, new TypeReference<WxQRTicketVO>(){});
        String QRCodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
                + wechatQRTicketVO.getTicket();
        log.info("QRCodeUrl = {}", QRCodeUrl);

        return QRCodeUrl;
    }


    public void updateAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&"
                + "appid=" + WxConfig.APP_ID
                + "&secret=" + WxConfig.APP_SECRET;

        HttpResult httpResult = httpAPIService.doGet(url);
        Integer statusCode = httpResult.getCode();
        String body = httpResult.getBody();
        String access_token = null;

        if (statusCode == 200) {
            access_token = JSON.parseObject(body).getString("access_token");
            if (access_token != null) {
                log.info("access_token = {}", access_token);
                AccessTokenCache.update(access_token);
            }
        }
    }
}
