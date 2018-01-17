package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.model.SysRole;
import com.cmazxiaoma.wx.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/13.
*/
@RestController
@RequestMapping("/sys_role")
public class SysRoleController{
    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        sysRoleService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody SysRole sysRole) {
        sysRoleService.update(sysRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.findById(id);
        return ResultVOGenerator.genSuccessResult(sysRole);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysRole> list = sysRoleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
