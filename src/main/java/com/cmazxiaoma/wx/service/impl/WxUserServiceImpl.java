package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.WxUserMapper;
import com.cmazxiaoma.wx.model.WxUser;
import com.cmazxiaoma.wx.service.WxUserService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
@Service
@Transactional
public class WxUserServiceImpl extends AbstractService<WxUser> implements WxUserService {
    @Resource
    private WxUserMapper wxUserMapper;

}
