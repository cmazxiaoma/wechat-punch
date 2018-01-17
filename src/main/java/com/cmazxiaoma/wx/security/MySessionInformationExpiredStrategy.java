package com.cmazxiaoma.wx.security;

import com.alibaba.fastjson.JSON;
import com.cmazxiaoma.wx.core.ResultCode;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;


@Slf4j
public class MySessionInformationExpiredStrategy implements InvalidSessionStrategy, SessionInformationExpiredStrategy {
    //session失效跳转的目标地址
    private String destinationUrl;
    //重定向策略
    private RedirectStrategy redirectStrategy;

    @Setter
    private boolean createNewSession = true;

    public MySessionInformationExpiredStrategy(String invalidSessionUrl) {
        this(invalidSessionUrl, new DefaultRedirectStrategy());
    }

    public MySessionInformationExpiredStrategy(String invalidSessionUrl, RedirectStrategy redirectStrategy) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl),
                "url must start with '/' or with 'http(s)'");
        this.destinationUrl = invalidSessionUrl;
        this.redirectStrategy = redirectStrategy;
    }


    /**
     * 无效session
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        log.info("Starting new session (if required) and redirecting to '"
                + destinationUrl + "'");

        if (createNewSession) {
            httpServletRequest.getSession();
        }

        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        out.write(JSON.toJSONString(ResultVOGenerator.genCustomResult(ResultCode.INVALID_SESSION)));
        out.flush();
        out.close();
    }

    /**
     * session过期 (多次登录)
     * @param sessionInformationExpiredEvent 过期session信息事件
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        log.info("Redirecting to '" + this.destinationUrl + "'");
        SessionInformation sessionInformation = sessionInformationExpiredEvent.getSessionInformation();

        log.info("isExpired = {}", sessionInformation.isExpired());

        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();

        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        out.write(JSON.toJSONString(ResultVOGenerator.genCustomResult(ResultCode.ACCOUNT_LOGIN_ELSEWHERE)));
        out.flush();
        out.close();
    }
}
