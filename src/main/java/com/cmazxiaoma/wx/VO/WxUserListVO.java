package com.cmazxiaoma.wx.VO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxUserListVO {

    /**
     * total : 2
     * count : 2
     * data : {"openid":["oTyMJxP5hi0Yq4qHb6Mlh4oT7M9Q","oTyMJxBQ2mqjPC8tEdssdVriapC0"]}
     * next_openid : oTyMJxBQ2mqjPC8tEdssdVriapC0
     */

    private int total;
    private int count;
    private DataBean data;
    private String next_openid;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getNext_openid() {
        return next_openid;
    }

    public void setNext_openid(String next_openid) {
        this.next_openid = next_openid;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataBean {
        private List<String> openid;

        public List<String> getOpenid() {
            return openid;
        }

        public void setOpenid(List<String> openid) {
            this.openid = openid;
        }
    }
}
