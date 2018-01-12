package com.cmazxiaoma.wechat.controller.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmazxiaoma.wechat.constant.WechatConfig;
import com.cmazxiaoma.wechat.core.BaseController;
import com.cmazxiaoma.wechat.http.HttpAPIService;
import com.cmazxiaoma.wechat.http.HttpResult;
import com.cmazxiaoma.wechat.model.WxUser;
import com.cmazxiaoma.wechat.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/wx_callback")
@Slf4j
public class WxCallBackController extends BaseController {

    @Autowired
    private HttpAPIService httpAPIService;

    @Resource
    private WxUserService wxUserService;


    @GetMapping()
    public String doGetCallBack() {
        //用户同意授权之后，获得code
        //示例:code = 011zu7Q30C6eiD1zJBP30TgUP30zu7Qz
        String code = request.getParameter("code");
        log.info("code = {}", code);

        //通过code获取openid,access_token
        //access_token有效期为2小时，网页授权接口调用凭证，此access_token与基础支持的access_token不同
        //openid 用户唯一标识
        //refresh_token 用户刷新access_token会使用到,refresh_token有效期为30天
        //scope 用户授权的作用域
        //snsapi_base不弹出授权界面，直接跳转只能获取用户openid
        //snsapi_userinfo 弹出授权界面，可通过openid拿到
        // country,province,city, openid,sex,nickname,headimgurl,language,privilege(特权)等信息
        /*
        返回的结果示例:
        { "access_token":"ACCESS_TOKEN",
           "expires_in":7200,
            "refresh_token":"REFRESH_TOKEN",
            "openid":"OPENID",
            "scope":"SCOPE"
         }
         */
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WechatConfig.APP_ID
                + "&secret="+ WechatConfig.APP_SECRET
                + "&code=" + code
                + "&grant_type=authorization_code";
        HttpResult httpResult = httpAPIService.doGet(url);

        log.info("content = {}", httpResult.getBody());
        JSONObject jsonObject = JSON.parseObject(httpResult.getBody());
        String openid = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");

        //获取用户基础信息
        //country,province,city, openid,sex,nickname,headimgurl,language,privilege
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + token
                + "&openid=" + openid
                + "&lang=zh_CN";

        JSONObject userInfo = JSON.parseObject(
                httpAPIService.doGet(infoUrl)
                        .getBody());

        log.info("userInfo = {}", userInfo);
        String city = userInfo.getString("city");
        String country = userInfo.getString("country");
        String province = userInfo.getString("province");
        String headImgUrl = userInfo.getString("headimgurl");
        String language = userInfo.getString("language");
        String nickName = userInfo.getString("nickname");
        String privilege = userInfo.getString("privilege");
        String sex = userInfo.getString("sex");

        //通过openid判断数据库是否已经该用户
        WxUser dbWxUser = wxUserService.findBy("openid", openid);

        if (dbWxUser == null) {
            WxUser wxUser = new WxUser();
            wxUser.setCity(city);
            wxUser.setCountry(country);
            wxUser.setProvince(province);
            wxUser.setCreateDate(new Date());
            wxUser.setHeadimgurl(headImgUrl);
            wxUser.setLanguage(language);
            wxUser.setNickName(nickName);
            wxUser.setOpenid(openid);
            wxUser.setPrivilege(privilege);
            wxUser.setSex(sex);
            wxUser.setUpdateDate(new Date());
            wxUserService.save(wxUser);
        } else {
            dbWxUser.setCity(city);
            dbWxUser.setCountry(country);
            dbWxUser.setProvince(province);
            dbWxUser.setUpdateDate(new Date());
            dbWxUser.setHeadimgurl(headImgUrl);
            dbWxUser.setLanguage(language);
            dbWxUser.setNickName(nickName);
            dbWxUser.setOpenid(openid);
            dbWxUser.setPrivilege(privilege);
            dbWxUser.setSex(sex);
            wxUserService.update(dbWxUser);
        }

        modelMap.addAttribute("userInfo", userInfo);
        return "authorized_success";
    }

    @PostMapping()
    public void doPostCallBack() {

    }
}
