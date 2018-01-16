package com.cmazxiaoma.wx.security;

import com.cmazxiaoma.wx.dao.SysPermissionMapper;
import com.cmazxiaoma.wx.model.SysPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import sun.security.krb5.Config;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
//作用:从数据库提取权限和资源，装配到map里
public class MyInvocationSecurityMetaDataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    private Map<String, Collection<ConfigAttribute>> map = null;

    //加载所有资源与权限的关系
    public void loadResourceDefine() {
        map = new HashMap<>();
        Collection<ConfigAttribute> configAttributeCollection = null;
        ConfigAttribute configAttribute = null;
        List<SysPermission> sysPermissionList = sysPermissionMapper.selectAll();

        for (SysPermission sysPermission : sysPermissionList) {
            configAttributeCollection = new ArrayList<>();
            configAttribute = new SecurityConfig(sysPermission.getName());
            configAttributeCollection.add(configAttribute);
            log.info("sysPermission_url = {}", sysPermission.getUrl());
            map.put(sysPermission.getUrl(), configAttributeCollection);
        }
    }


    //此方法是为了判定用户请求的url是否在权限表中，如果在权限表中，则返回给decide方法，判定用户是否拥有此权限。
    //如果不在权限表中，则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        if (map == null) {
            loadResourceDefine();
        }
        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        AntPathRequestMatcher antPathRequestMatcher = null;
        String resUrl = null;

        Iterator<Map.Entry<String, Collection<ConfigAttribute>>> iter = map.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, Collection<ConfigAttribute>> entry = iter.next();
            resUrl = entry.getKey();

            log.info("resUrl = {}", resUrl);
            antPathRequestMatcher = new AntPathRequestMatcher(resUrl);

            if (antPathRequestMatcher.matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
