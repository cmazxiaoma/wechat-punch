package com.cmazxiaoma.wechat.http;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult {

    Integer code = 400;

    String body;
}
