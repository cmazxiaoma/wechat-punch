package com.cmazxiaoma.wx.dao;

import com.cmazxiaoma.wx.core.Mapper;
import com.cmazxiaoma.wx.model.WxUserTimeSheet;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WxUserTimeSheetMapper extends Mapper<WxUserTimeSheet> {

    List<WxUserTimeSheet> findByOpenIdOrNickName(Map<String, String> params);
}