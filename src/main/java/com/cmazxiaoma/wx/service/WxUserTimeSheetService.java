package com.cmazxiaoma.wx.service;
import com.cmazxiaoma.wx.model.WxUserTimeSheet;
import com.cmazxiaoma.wx.core.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by cmazxiaoma on 2018/01/15.
 */
public interface WxUserTimeSheetService extends Service<WxUserTimeSheet> {

    List<WxUserTimeSheet> findByOpenIdOrNickName(Map<String, String> params);

    WxUserTimeSheet findByOpenIdAndPunchDate(String openId, Date punchDate);
}
