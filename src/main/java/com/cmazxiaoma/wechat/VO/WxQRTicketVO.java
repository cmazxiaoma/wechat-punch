package com.cmazxiaoma.wechat.VO;

import lombok.*;
import org.aspectj.lang.annotation.DeclareAnnotation;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class WxQRTicketVO {

    /**
     * ticket : gQHE8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyWmhCZDVIZnpjOTQxMDAwMHcwM2sAAgQT2FVaAwQAAAAA
     * url : http://weixin.qq.com/q/02ZhBd5Hfzc9410000w03k
     */

    private String ticket;
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
