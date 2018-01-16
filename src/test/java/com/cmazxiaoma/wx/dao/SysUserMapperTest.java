package com.cmazxiaoma.wx.dao;

import com.cmazxiaoma.wx.model.SysUser;
import com.cmazxiaoma.wx.service.SysUserService;
import com.conpany.project.BaseTester;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class SysUserMapperTest extends BaseTester {

    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    public void testExample() {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", "cmazxiaoma");
        SysUser sysUser = sysUserMapper.selectOneByExample(example);
        log.info("sysUser={}", sysUser);
    }
}