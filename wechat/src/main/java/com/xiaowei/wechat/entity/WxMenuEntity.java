package com.xiaowei.wechat.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Table(name = "wx_menu")
@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class WxMenuEntity {

    public WxMenuEntity() {
    }

    public WxMenuEntity(String menuId, String data) {
        this.menuId = menuId;
        this.data = data;
    }

    /**
     * 菜单id
     */
    @Id
    String menuId;

    /**
     * 配置时的菜单json
     */
    @Lob
    String data;

}
