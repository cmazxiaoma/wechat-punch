package com.cmazxiaoma.wechat.util.wechat;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return SpringContextUtil.applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return SpringContextUtil.getContext().getBean(requiredType);
    }
}
