package com.cmazxiaoma.wechat.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsMessage extends BaseMessage {
    int ArticleCount;
    List<News> Articles;
}
