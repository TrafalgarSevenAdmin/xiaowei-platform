package com.xiaowei.mq.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserChageMassage implements MessageBean {

    public UserChageMassage(String userId) {
        this.userId = userId;
        this.type = Type.Chage;
    }

    public UserChageMassage(String userId, String openId, Type type) {
        this.userId = userId;
        this.openId = openId;
        this.type = type;
    }

    String userId;

    String openId;

    Type type = Type.Chage;

    public static enum Type implements Serializable {

        /**
         * 绑定微信
         */
        Bind,
        /**
         * 用户信息变更
         */
        Chage,

        /**
         * 用户删除
         */
        Delete
    }

}
