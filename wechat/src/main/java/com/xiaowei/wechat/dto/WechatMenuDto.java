package com.xiaowei.wechat.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("微信菜单设置的请求")
@Data
public class WechatMenuDto {

    public List<OneLevelMenu> menus;

    public static class OneLevelMenu {
        public OneLevelMenu() {
        }

        String id;
         String name;
         List<String> subButtons;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getSubButtons() {
            return subButtons;
        }

        public void setSubButtons(List<String> subButtons) {
            this.subButtons = subButtons;
        }
    }
}
