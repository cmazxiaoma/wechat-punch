package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.model.PunchRole;
import com.cmazxiaoma.wx.service.PunchRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by cmazxiaoma on 2018/01/15.
*/
@RestController
@RequestMapping("/punch_role")
public class PunchRoleController{
    @Resource
    private PunchRoleService punchRoleService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody PunchRole punchRole) {
        punchRoleService.save(punchRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        punchRoleService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody PunchRole punchRole) {
        punchRoleService.update(punchRole);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        PunchRole punchRole = punchRoleService.findById(id);
        return ResultVOGenerator.genSuccessResult(punchRole);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PunchRole> list = punchRoleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
