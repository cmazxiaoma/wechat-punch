package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.SysPermissionMapper;
import com.cmazxiaoma.wx.dao.SysUserMapper;
import com.cmazxiaoma.wx.model.SysPermission;
import com.cmazxiaoma.wx.model.SysUser;
import com.cmazxiaoma.wx.service.SysUserService;
import com.cmazxiaoma.wx.core.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
@Slf4j
public class SysUserServiceImpl extends AbstractService<SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        SysUser sysUser = sysUserMapper.selectOneByExample(example);

        if (sysUser != null) {
            List<SysPermission> permissionList = sysPermissionMapper.findByUserId(sysUser.getId());
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

            for (SysPermission sysPermission : permissionList) {
                if (sysPermission != null && sysPermission.getName() != null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getName());
                    //将权限信息添加到GrantedAuthority对象中，在后面进行全权限验证时会使用到
                    grantedAuthorityList.add(grantedAuthority);
                }
            }

            sysUser.setAuthorities(grantedAuthorityList);
            return sysUser;
        } else {
            throw new UsernameNotFoundException("此" +  username + "用户不存在");
        }
    }
}
