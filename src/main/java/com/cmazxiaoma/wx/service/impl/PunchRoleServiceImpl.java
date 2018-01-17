package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.PunchRoleMapper;
import com.cmazxiaoma.wx.model.PunchRole;
import com.cmazxiaoma.wx.service.PunchRoleService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/15.
 */
@Service
@Transactional
public class PunchRoleServiceImpl extends AbstractService<PunchRole> implements PunchRoleService {
    @Resource
    private PunchRoleMapper punchRoleMapper;

}
