package com.cmazxiaoma.wx.service.impl;

import com.cmazxiaoma.wx.model.WxUserTimeSheet;
import com.cmazxiaoma.wx.service.WxUserTimeSheetService;
import com.conpany.project.BaseTester;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class WxUserTimeSheetServiceImplTest extends BaseTester {

    @Resource
    private WxUserTimeSheetService wxUserTimeSheetService;

    @Test
    public void findByOpenIdAndPunchDate() throws Exception {
        WxUserTimeSheet wxUserTimeSheet = wxUserTimeSheetService.findByOpenIdAndPunchDate("oTyMJxBQ2mqjPC8tEdssdVriapC0", DateUtils.parseDate("2018-01-15", "yyyy-MM-dd"));
        System.out.println(wxUserTimeSheet);
    }

}