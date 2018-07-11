package com.xiaowei.wechat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.account.entity.SysUser;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "WX_USER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class WxUser {

    /**
     * openId
     * 主键
     */
    @Id
    private String openId;

    /**
     * 是否订阅
     */
    private Boolean subscribe;


    /**
     * 真实昵称
     */
    private String nickname;

    /**
     * 性别描述信息：男、女、未知等.
     */
    private String sexDesc;

    /**
     * 性别表示：1，2等数字.
     */
    private Integer sex;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像地址（如果用户换了头像，那么这个地址就会失效，所以以后再考虑保存到本地）
     */
    private String headImgUrl;

    /**
     * 关注时间（时间搓）
     */
    private Long subscribeTime;

    /**
     * 取消关注时间
     */
    private Date unsubscribeTime;

    /**
     * https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&announce_id=11513156443eZYea&version=&lang=zh_CN
     * <pre>
     * 只有在将公众号绑定到微信开放平台帐号后，才会出现该字段。
     * 另外，在用户未关注公众号时，将不返回用户unionID信息。
     * 已关注的用户，开发者可使用“获取用户基本信息接口”获取unionID；
     * 未关注用户，开发者可使用“微信授权登录接口”并将scope参数设置为snsapi_userinfo，获取用户unionID
     * </pre>
     */
    private String unionId;

    /**
     * 描述
     */
    private String remark;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 标签
     */
    private Long[] tagIds;

    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）.
     */
    private String[] privileges;

    /**
     * subscribe_scene 返回用户关注的渠道来源.
     * ADD_SCENE_SEARCH 公众号搜索，
     * ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
     * ADD_SCENE_PROFILE_CARD 名片分享，
     * ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENEPROFILE LINK 图文页内名称点击，
     * ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，
     * ADD_SCENE_OTHERS 其他
     */
    private String subscribeScene;

    /**
     * 对应的系统用户
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "sys_user_id")
    @Fetch(FetchMode.JOIN)
    private SysUser sysUser;

}
