package com.cmazxiaoma.wx.service;
import com.cmazxiaoma.wx.model.SysUser;
import com.cmazxiaoma.wx.core.Service;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * Created by cmazxiaoma on 2018/01/13.
 */
public interface SysUserService extends Service<SysUser>, UserDetailsService {

}
