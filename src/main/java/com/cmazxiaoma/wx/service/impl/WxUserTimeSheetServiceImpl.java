package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.dao.WxUserTimeSheetMapper;
import com.cmazxiaoma.wx.model.WxUserTimeSheet;
import com.cmazxiaoma.wx.service.WxUserTimeSheetService;
import com.cmazxiaoma.wx.core.AbstractService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by cmazxiaoma on 2018/01/15.
 */
@Service
@Transactional
public class WxUserTimeSheetServiceImpl extends AbstractService<WxUserTimeSheet> implements WxUserTimeSheetService {
    @Resource
    private WxUserTimeSheetMapper wxUserTimeSheetMapper;

    @Override
    public List<WxUserTimeSheet> findByOpenIdOrNickName(Map<String, String> params) {
        return wxUserTimeSheetMapper.findByOpenIdOrNickName(params);
    }

    @Override
    public WxUserTimeSheet findByOpenIdAndPunchDate(String openId, Date punchDate) {
        Condition condition = new Condition(WxUserTimeSheet.class);
        condition.createCriteria()
                .andEqualTo("openId", openId)
                .andEqualTo("punchDate", DateFormatUtils.format(punchDate, "yyyy-MM-dd"));

        return wxUserTimeSheetMapper.selectOneByExample(condition);
    }
}
