package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.SysPermissionRoleMapper;
import com.cmazxiaoma.wx.model.SysPermissionRole;
import com.cmazxiaoma.wx.service.SysPermissionRoleService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
public class SysPermissionRoleServiceImpl extends AbstractService<SysPermissionRole> implements SysPermissionRoleService {
    @Resource
    private SysPermissionRoleMapper sysPermissionRoleMapper;

}
