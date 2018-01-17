package com.cmazxiaoma.wx.configure;

import com.alibaba.fastjson.JSON;
import com.cmazxiaoma.wx.core.ResultCode;
import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.security.*;
import com.cmazxiaoma.wx.service.*;
import com.cmazxiaoma.wx.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private MyInvocationSecurityMetaDataSourceService myInvocationSecurityMetaDataSourceService;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    private MyAuthenticationAccessDeniedHandler myAuthenticationAccessDeniedHandler;

    @Resource
    private SysUserService sysUserService;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    //session失效跳转
    private MySessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new MySessionInformationExpiredStrategy("/login");
    }


    //Spring Security内置的session监听器
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication()
                //.withUser("user")
                //.password("password")
                //.roles("USER");

        //MD5加密
        auth.userDetailsService(sysUserService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                log.info("encode- charSequence=" + charSequence);
                return MD5Util.encode((String) charSequence);
            }

            //校验密码
            @Override
            public boolean matches(CharSequence charSequence, String encodedPassword) {
                log.info("matches - charSequence=" + charSequence);
                log.info("matches - encodedPassoword=" + encodedPassword);
                return encodedPassword.equals(MD5Util.encode((String) charSequence));
            }
        });
        //注入事件发布者
        auth.authenticationEventPublisher(authenticationEventPublisher());

    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    //定义那些url不需要被保护
    @Override
    public void configure(WebSecurity web) throws Exception {
       web.ignoring().antMatchers(
               "/static/**",
               "/wxjs_config",
               "/wx_user_time_sheet/**",
               "/punch_role/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭CSRF,Spring Security4.0之后，引入了CSRF,默认是开启的。CSRF和RESTful
        //技术有冲突,CSRF默认支持的方法:GET|HEAD|TRACE|OPTIONS,不支持POST
        http.csrf().disable();

//        //session并发控制过滤器
//        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry(), sessionInformationExpiredStrategy()), ConcurrentSessionFilter.class);

        http.authorizeRequests()
                /*.antMatchers("/wx_home").access("hasAuthority('QUERY_ALL_USER_INFO')")*/
                //任何请求，只有登录后才能访问
                .anyRequest().authenticated()
                //session并发管理，用户只能在一个终端登录
                .and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry())
                //多处登录处理策略
                .expiredSessionStrategy(sessionInformationExpiredStrategy())
                //session失效策略
                .and().invalidSessionStrategy(sessionInformationExpiredStrategy())
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/query_currentuser_info")
                .failureUrl("/login?error")
                .permitAll()
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                        httpServletResponse.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = httpServletResponse.getWriter();
//                        ResultVO resultVO = null;
//
//                        if (e instanceof UsernameNotFoundException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.USERNAME_ERROR);
//                        } else if (e instanceof BadCredentialsException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.PASSWORD_ERROR);
//                        } else if (e instanceof LockedException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.ACCOUNT_LOCAKED);
//                        } else if (e instanceof DisabledException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.ACCOUNT_DISABLED);
//                        } else {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.UNKNOWN_ERROR);
//                        }
//
//                        out.write(JSON.toJSONString(resultVO));
//                        out.flush();
//                        out.close();
//                    }
//                })
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                        httpServletResponse.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = httpServletResponse.getWriter();
//                        out.write(JSON.toJSONString(ResultVOGenerator.genSuccessResult("login success")));
//                        out.flush();
//                        out.close();
//                    }
//                })
                .and().logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and().addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                //配置未授权处理的handler
                .exceptionHandling().accessDeniedHandler(myAuthenticationAccessDeniedHandler)
                .and().httpBasic();
    }
}
