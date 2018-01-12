package com.cmazxiaoma.wechat.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//注解开启Spring Security的功能,继承WebSecurityConfigurerAdapter设置一些web安全的细节
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

}
