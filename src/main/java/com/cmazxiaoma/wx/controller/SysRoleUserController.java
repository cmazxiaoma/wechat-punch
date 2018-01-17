package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.model.SysRoleUser;
import com.cmazxiaoma.wx.service.SysRoleUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/13.
*/
@RestController
@RequestMapping("/sys_role_user")
public class SysRoleUserController{
    @Resource
    private SysRoleUserService sysRoleUserService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody SysRoleUser sysRoleUser) {
        sysRoleUserService.save(sysRoleUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        sysRoleUserService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody SysRoleUser sysRoleUser) {
        sysRoleUserService.update(sysRoleUser);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        SysRoleUser sysRoleUser = sysRoleUserService.findById(id);
        return ResultVOGenerator.genSuccessResult(sysRoleUser);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysRoleUser> list = sysRoleUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
