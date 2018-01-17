package com.cmazxiaoma.wx.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent abstractAuthenticationEvent) {
        log.info("Receive event of type:"
                + abstractAuthenticationEvent.getClass().getName() + ":" + abstractAuthenticationEvent.toString());
    }
}
