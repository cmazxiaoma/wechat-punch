package com.cmazxiaoma.wx.VO;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class WxUserTimeSheetVO {
    @JSONField(name = "nick_name", ordinal = 1)
    private String nickName;

    @JSONField(name = "location_info", ordinal = 2)
    private String locationInfo;

    @JSONField(name = "first_punch_time", format="HH:mm:ss", ordinal = 3)
    private Date firstPunchTime;

    @JSONField(name = "last_punch_time", format="HH:mm:ss", ordinal = 4)
    private Date lastPunchTime;

    @JSONField(name = "punch_date", format="yyyy-MM-dd 星期u", ordinal = 5)
    private Date punchDate;

    @JSONField(name = "first_status", ordinal = 6)
    private String firstStatus;

    @JSONField(name = "last_status", ordinal = 7)
    private String lastStatus;

    @JSONField(serialize = false)
    private Integer statusCode;

    public WxUserTimeSheetVO(String nickName, String locationInfo, Date firstPunchTime,
                             Date lastPunchTime, Date punchDate, Integer firstStatusCode, Integer lastStatusCode) {
        this.nickName = nickName;
        this.locationInfo = locationInfo;
        this.firstPunchTime = firstPunchTime;
        this.lastPunchTime = lastPunchTime;
        this.punchDate = punchDate;
        this.firstStatus = getStatus(firstStatusCode);
        this.lastStatus = getStatus(lastStatusCode);
    }

    private String getStatus(Integer statusCode) {
        if (statusCode == null) {
            throw new NullPointerException("statuCode为空");
        }
        String status = null;
        switch (statusCode) {
            case -1:
                status = "未考勤";
                break;
            case 0:
                status = "正常";
                break;
            case 1:
                status = "迟到";
                break;
            case 2:
                status = "早退";
                break;
            case 3:
                status = "加班";
                break;
            case 4:
                status = "请假";
                break;
        }

        return status;
    }
}
