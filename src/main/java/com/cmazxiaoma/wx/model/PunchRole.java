package com.cmazxiaoma.wx.model;

import javax.persistence.*;

@Table(name = "punch_role")
public class PunchRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gowork_date")
    private String goworkDate;

    @Column(name = "offwork_date")
    private String offworkDate;

    @Column(name = "overtime_start_date")
    private String overtimeStartDate;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return gowork_date
     */
    public String getGoworkDate() {
        return goworkDate;
    }

    /**
     * @param goworkDate
     */
    public void setGoworkDate(String goworkDate) {
        this.goworkDate = goworkDate;
    }

    /**
     * @return offwork_date
     */
    public String getOffworkDate() {
        return offworkDate;
    }

    /**
     * @param offworkDate
     */
    public void setOffworkDate(String offworkDate) {
        this.offworkDate = offworkDate;
    }

    /**
     * @return overtime_start_date
     */
    public String getOvertimeStartDate() {
        return overtimeStartDate;
    }

    /**
     * @param overtimeStartDate
     */
    public void setOvertimeStartDate(String overtimeStartDate) {
        this.overtimeStartDate = overtimeStartDate;
    }
}