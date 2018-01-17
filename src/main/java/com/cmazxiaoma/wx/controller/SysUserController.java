package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.model.SysUser;
import com.cmazxiaoma.wx.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/13.
*/
@RestController
@RequestMapping("/sys_user")
public class SysUserController{
    @Resource
    private SysUserService sysUserService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        sysUserService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody SysUser sysUser) {
        sysUserService.update(sysUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        SysUser sysUser = sysUserService.findById(id);
        return ResultVOGenerator.genSuccessResult(sysUser);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysUser> list = sysUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
