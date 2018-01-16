package com.cmazxiaoma.wx.controller.wx;

import com.cmazxiaoma.wx.constant.WxConfig;
import com.cmazxiaoma.wx.core.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
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
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WxConfig.APP_ID
                + "&redirect_uri="+ encodedCallBackUrl
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";

        return new ModelAndView(new RedirectView(url));
    }

    @GetMapping("/wx_home")
    public String forwardWxHome() {
        return "wx_home";
    }

    @GetMapping("/wx/web/dev")
    public String forwardWxWebDev() {
        return "wx_web_dev";
    }
}
