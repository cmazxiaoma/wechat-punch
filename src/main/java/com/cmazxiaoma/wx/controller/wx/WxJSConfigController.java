package com.cmazxiaoma.wx.controller.wx;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.util.WxJSTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wxjs_config")
@Slf4j
public class WxJSConfigController {

    @PostMapping
    public ResultVO getWxJSSDKConfig(@RequestParam("url") String url) {
        if (StringUtils.isEmpty(url)) {
            return ResultVOGenerator.genFailResult("非法url");
        }

        log.info("url = {}", url);
        return WxJSTokenUtil.getInstance().genJSSignture(url);
    }
}
