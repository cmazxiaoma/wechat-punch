package com.cmazxiaoma.wx.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * decide方法是判定是否拥有权限的决策方法
     * @param authentication 是释放SysUserService中循环添加到GrantedAuthority对象中的权限信息集合
     * @param o 包含客户端发起请求的request信息,HttpServletRequest request = ((FilterInvocation) object).getHttpRequest;
     * @param collection 为MyInvocationSecurityMetadataSource的getAttribute(Object object)这个方法返回的结果
     *                   此方法是了判定用户请求的url是否在权限表中，如果在权限表中，则返回给decide方法，用来判定用户是否有此权限
     *                   如果不在权限表中则放行。
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if (collection == null || collection.size() <= 0) {
            return;
        }
        ConfigAttribute configAttribute = null;
        String needRole = null;
        //所请求的资源拥有的权限(一个url对应多个权限)
        for (Iterator<ConfigAttribute> iter = collection.iterator(); iter.hasNext();) {
            configAttribute = iter.next();
            //访问该资源所需要的权限
            needRole = configAttribute.getAttribute();
            log.info("needRole = {}", needRole);

            //用户所拥有的权限
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                String sysPermissionName = grantedAuthority.getAuthority();
                log.info("sysPermission_name={}", sysPermissionName);

                if (StringUtils.equals(sysPermissionName, needRole.trim())) {
                    return;
                }
            }
        }

        //用户没有权限访问该资源
        log.info("access denied");
        throw new AccessDeniedException("您没有该" + needRole + "权限");
    }

    //表示当前AccessDecisionManager是否支持对应的ConfigAttribute
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    //表示当前AccessDecisionManager是否支持对应的受保护类型
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
