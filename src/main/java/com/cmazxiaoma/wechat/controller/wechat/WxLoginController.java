package com.cmazxiaoma.wechat.controller.wechat;

import com.cmazxiaoma.wechat.constant.WechatConfig;
import com.cmazxiaoma.wechat.core.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@Slf4j
public class WxLoginController extends BaseController {

    @GetMapping("/wxauth/wxlogin")
    public ModelAndView doGetWxLogin() throws IOException {
        //回调地址
        String callBackUrl = "http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/wx_callback";
        String encodedCallBackUrl = URLEncoder.encode(callBackUrl, "UTF-8");

        log.info("encodedCallBackUrl = {}", encodedCallBackUrl);
        //发起授权请求
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WechatConfig.APP_ID
                + "&redirect_uri="+ encodedCallBackUrl
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";

        return new ModelAndView(new RedirectView(url));
    }

    @GetMapping("/wxauth/pclogin")
    public void doGetWxPcLogin() {

    }

    @GetMapping("/home")
    public String forwardHome() {
        return "home";
    }

}
