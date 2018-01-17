package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.SysPermissionMapper;
import com.cmazxiaoma.wx.model.SysPermission;
import com.cmazxiaoma.wx.service.SysPermissionService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
public class SysPermissionServiceImpl extends AbstractService<SysPermission> implements SysPermissionService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;

}
