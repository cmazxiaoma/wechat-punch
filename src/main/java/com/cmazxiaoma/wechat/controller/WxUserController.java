package com.cmazxiaoma.wechat.controller;

import com.cmazxiaoma.wechat.core.ResultVO;
import com.cmazxiaoma.wechat.core.ResultVOGenerator;
import com.cmazxiaoma.wechat.model.WxUser;
import com.cmazxiaoma.wechat.service.WxUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/12.
*/
@RestController
@RequestMapping("/wx/user")
public class WxUserController{
    @Resource
    private WxUserService wxUserService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody WxUser wxUser) {
        wxUserService.save(wxUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        wxUserService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody WxUser wxUser) {
        wxUserService.update(wxUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        WxUser wxUser = wxUserService.findById(id);
        return ResultVOGenerator.genSuccessResult(wxUser);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WxUser> list = wxUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
