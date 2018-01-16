package com.cmazxiaoma.wx.security;

import com.cmazxiaoma.wx.security.MyAccessDecisionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;

@Service
@Slf4j
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    @Autowired
    private void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(filterInvocation);
    }

    public void invoke(FilterInvocation filterInvocation) {
        /**
         * filterInvocation里面被拦截的url
         * 里面调用MyInvocationSecurityMetadataSource的getAttribute(Object object)这个方法获取filterInvocation所对应的权限
         * 再调用MyAccessDecisionManager的decide()方法来校验用户的权限是否足够
         */

        //1.获取请求资源的权限 Collection<ConfigAttribute> attributes = filterInvocationSecurityMetadataSource.getAttribute(filterInvocation)
        //2.是否拥有权限 myAccessDecisionManager.decide(authentication, filterInvocation, attributes)
        InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
        log.info("token={}", token);

        try {
            //执行下一个过滤器
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } catch (Exception e) {
            log.info("执行过滤器失败 = {}", e.getMessage());
            if (e instanceof AccessDeniedException) {
                throw new AccessDeniedException("Access is denied");
            } else{
                throw new RuntimeException(e.getMessage());
            }

        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.filterInvocationSecurityMetadataSource;
    }
}
