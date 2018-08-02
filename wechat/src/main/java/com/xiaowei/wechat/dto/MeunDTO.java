package com.xiaowei.wechat.dto;

import lombok.Data;
import me.chanjar.weixin.common.bean.menu.WxMenu;

import java.util.List;

@Data
public class MeunDTO {
    List<MeunDetail> meuns;

    public static class MeunDetail{
        WxMenu menu;
        String tagName;

        public WxMenu getMenu() {
            return menu;
        }

        public void setMenu(WxMenu menu) {
            this.menu = menu;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }
}
