package com.cmazxiaoma.wechat.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationMessage extends BaseMessage {
    String Location_X;
    String Location_Y;
    String Scale;
    String Label;
    String MsgId;
}
