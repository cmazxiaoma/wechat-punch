package com.cmazxiaoma.wx.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wx_user_time_sheet")
@Data
public class WxUserTimeSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "location_latitude")
    private String locationLatitude;

    @Column(name = "location_longitude")
    private String locationLongitude;

    @Column(name = "location_info")
    private String locationInfo;

    /**
     * -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    @Column(name = "first_status")
    private Integer firstStatus;

    /**
     * -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    @Column(name = "last_status")
    private Integer lastStatus;

    @Column(name = "first_punch_time")
    private Date firstPunchTime;

    @Column(name = "last_punch_time")
    private Date lastPunchTime;

    @Column(name = "punch_date")
    private Date punchDate;

    @Transient
    private String nickName;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return open_id
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return location_latitude
     */
    public String getLocationLatitude() {
        return locationLatitude;
    }

    /**
     * @param locationLatitude
     */
    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    /**
     * @return location_longitude
     */
    public String getLocationLongitude() {
        return locationLongitude;
    }

    /**
     * @param locationLongitude
     */
    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    /**
     * @return location_info
     */
    public String getLocationInfo() {
        return locationInfo;
    }

    /**
     * @param locationInfo
     */
    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    /**
     * 获取-1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     *
     * @return first_status - -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    public Integer getFirstStatus() {
        return firstStatus;
    }

    /**
     * 设置-1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     *
     * @param firstStatus -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    public void setFirstStatus(Integer firstStatus) {
        this.firstStatus = firstStatus;
    }

    /**
     * 获取-1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     *
     * @return last_status - -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    public Integer getLastStatus() {
        return lastStatus;
    }

    /**
     * 设置-1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     *
     * @param lastStatus -1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假
     */
    public void setLastStatus(Integer lastStatus) {
        this.lastStatus = lastStatus;
    }

    /**
     * @return first_punch_time
     */
    public Date getFirstPunchTime() {
        return firstPunchTime;
    }

    /**
     * @param firstPunchTime
     */
    public void setFirstPunchTime(Date firstPunchTime) {
        this.firstPunchTime = firstPunchTime;
    }

    /**
     * @return last_punch_time
     */
    public Date getLastPunchTime() {
        return lastPunchTime;
    }

    /**
     * @param lastPunchTime
     */
    public void setLastPunchTime(Date lastPunchTime) {
        this.lastPunchTime = lastPunchTime;
    }

    /**
     * @return punch_date
     */
    public Date getPunchDate() {
        return punchDate;
    }

    /**
     * @param punchDate
     */
    public void setPunchDate(Date punchDate) {
        this.punchDate = punchDate;
    }
}