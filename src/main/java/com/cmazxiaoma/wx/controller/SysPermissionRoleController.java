package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.model.SysPermissionRole;
import com.cmazxiaoma.wx.service.SysPermissionRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/13.
*/
@RestController
@RequestMapping("/sys_permission_role")
public class SysPermissionRoleController{
    @Resource
    private SysPermissionRoleService sysPermissionRoleService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody SysPermissionRole sysPermissionRole) {
        sysPermissionRoleService.save(sysPermissionRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        sysPermissionRoleService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody SysPermissionRole sysPermissionRole) {
        sysPermissionRoleService.update(sysPermissionRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        SysPermissionRole sysPermissionRole = sysPermissionRoleService.findById(id);
        return ResultVOGenerator.genSuccessResult(sysPermissionRole);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysPermissionRole> list = sysPermissionRoleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
