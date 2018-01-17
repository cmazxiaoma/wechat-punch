package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.SysRoleUserMapper;
import com.cmazxiaoma.wx.model.SysRoleUser;
import com.cmazxiaoma.wx.service.SysRoleUserService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
public class SysRoleUserServiceImpl extends AbstractService<SysRoleUser> implements SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

}
