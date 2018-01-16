package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.VO.MsgInfoVO;
import com.cmazxiaoma.wx.core.BaseController;
import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@RestController
public class SpringSecurityPortalController extends BaseController {

    @GetMapping("/query_currentuser_info")
    public ModelAndView index() {
        MsgInfoVO msgInfo = new MsgInfoVO("测试标题", "测试内容", "具有QUERY_CURRENT_USER_INFO权限");
        modelMap.addAttribute("msgInfo", msgInfo);
        log.info("msgInfo={}", msgInfo);
        return new ModelAndView("query_currentuser_info");
    }

    @GetMapping("/query_all_user_info")
    public ResultVO queryAllUserInfo() {
        return ResultVOGenerator.genSuccessResult("have QUERY_ALL_USER_INFO permission");
    }
}
