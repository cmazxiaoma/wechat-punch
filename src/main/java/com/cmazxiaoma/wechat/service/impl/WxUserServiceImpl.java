package com.cmazxiaoma.wechat.service.impl;

import com.cmazxiaoma.wechat.dao.WxUserMapper;
import com.cmazxiaoma.wechat.model.WxUser;
import com.cmazxiaoma.wechat.service.WxUserService;
import com.cmazxiaoma.wechat.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by cmazxiaoma on 2018/01/12.
 */
@Service
@Transactional
public class WxUserServiceImpl extends AbstractService<WxUser> implements WxUserService {
    @Resource
    private WxUserMapper wxUserMapper;

}
