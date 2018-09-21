package com.xiaowei.wechat.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 邀请关注信息
 */
@Data
public class InvitationInfoDto implements Serializable {

    /**
     * 邀请后自动绑定的用户id
     */
    String user;

    /**
     * 邀请人id
     */
    String inviter;

    /**
     * 来源场景
     * 目前只有: WORK-ORDER-PROCESSING -- 工程师处理工单时邀请。因此默认
     */
    String scene = "WORK-ORDER-PROCESSING";
}
