package com.cmazxiaoma.wechat.controller.wechat;

import com.cmazxiaoma.wechat.core.ResultVO;
import com.cmazxiaoma.wechat.core.ResultVOGenerator;
import com.cmazxiaoma.wechat.util.wechat.WxJSTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wxjs_config")
public class WxJSConfigController {

    @PostMapping
    public ResultVO getWxJSSDKConfig(@RequestParam("url") String url) {
        if (StringUtils.isEmpty(url)) {
            return ResultVOGenerator.genFailResult("非法url");
        }
        return WxJSTokenUtil.getInstance().genJSSignture(url);
    }
}
