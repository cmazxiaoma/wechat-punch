package com.cmazxiaoma.wx.configure;

import com.cmazxiaoma.wx.security.MyAccessDecisionManager;
import com.cmazxiaoma.wx.security.MyAuthenticationAccessDeniedHandler;
import com.cmazxiaoma.wx.security.MyFilterSecurityInterceptor;
import com.cmazxiaoma.wx.security.MyInvocationSecurityMetaDataSourceService;
import com.cmazxiaoma.wx.service.*;
import com.cmazxiaoma.wx.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;

import javax.annotation.Resource;

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

        http.authorizeRequests()
                /*.antMatchers("/wx_home").access("hasAuthority('QUERY_ALL_USER_INFO')")*/
                //任何请求，只有登录后才能访问
                .anyRequest().authenticated()
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
//                        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.USERNAME_PASSWORD_ERROR);
//                        } else if (e instanceof DisabledException) {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.ACCOUNT_CLOSED);
//                        } else {
//                            resultVO = ResultVOGenerator.genCustomResult(ResultCode.LOGIN_FAILED);
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
                .and().formLogin().permitAll()
                .and().addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class).exceptionHandling()
                .accessDeniedHandler(myAuthenticationAccessDeniedHandler);
    }
}
