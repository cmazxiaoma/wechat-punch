package com.cmazxiaoma.wx.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserTimeSheetDTO {

    @JSONField(name = "openid", ordinal = 1)
    @NotNull (message="openid不能为空！")
    private String openId;

    @JSONField(name = "location_info", ordinal = 2)
    @NotNull (message="location_info不能为空！")
    private String locationInfo;

    @JSONField(name = "location_latitude", ordinal = 3)
    @NotNull (message="location_latitude不能为空！")
    private String locationLatitude;

    @JSONField(name = "location_longitude", ordinal = 4)
    @NotNull(message="location_longitude不能为空！")
    private String locationLongitude;


    @JSONField(name = "punch_time", format = "HH:mm:ss", ordinal = 5)
    @NotNull (message="punch_time不能为空！")
    private Date punchTime;
}
