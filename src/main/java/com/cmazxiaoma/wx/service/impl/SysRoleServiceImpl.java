package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.SysRoleMapper;
import com.cmazxiaoma.wx.model.SysRole;
import com.cmazxiaoma.wx.service.SysRoleService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
public class SysRoleServiceImpl extends AbstractService<SysRole> implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

}
