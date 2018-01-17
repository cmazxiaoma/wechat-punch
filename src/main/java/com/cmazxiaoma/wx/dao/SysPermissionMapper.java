package com.cmazxiaoma.wx.dao;

import com.cmazxiaoma.wx.core.Mapper;
import com.cmazxiaoma.wx.model.SysPermission;

import java.security.Permission;
import java.util.List;

public interface SysPermissionMapper extends Mapper<SysPermission> {

    List<SysPermission> findByUserId(Long userId);
}