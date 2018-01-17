package com.cmazxiaoma.wx.model;

import javax.persistence.*;

@Table(name = "sys_role_user")
public class SysRoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sys_user_id")
    private Long sysUserId;

    @Column(name = "sys_role_id")
    private Long sysRoleId;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return sys_user_id
     */
    public Long getSysUserId() {
        return sysUserId;
    }

    /**
     * @param sysUserId
     */
    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    /**
     * @return sys_role_id
     */
    public Long getSysRoleId() {
        return sysRoleId;
    }

    /**
     * @param sysRoleId
     */
    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }
}