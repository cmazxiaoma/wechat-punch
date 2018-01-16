package com.cmazxiaoma.wx.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {
    String ToUserName;
    String FromUserName;
    Long CreateTime;
    String MsgType;
}
