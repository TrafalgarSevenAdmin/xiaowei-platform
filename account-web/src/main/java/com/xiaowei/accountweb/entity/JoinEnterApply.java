package com.xiaowei.accountweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.entity.Tenement;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.commonjts.utils.GeometryUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * 入驻申请表
 * 根据前端
 */
@Data
@Table(name = "m_enter_apply")
@Entity
@SQLDelete(sql = "update m_enter_apply set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class JoinEnterApply extends MultiBaseEntity {

    @ApiModelProperty("用户名")
    String userName;

    @ApiModelProperty(value = "手机号")
    String mobilePhone;

    @ApiModelProperty("邮箱号")
    String email;

    @ApiModelProperty(value = "用户类型",notes = "个人、公司、个体工商户、工作室、项目组")
    String userType;

    @ApiModelProperty("QQ号")
    String qqNumber;

    @ApiModelProperty(value = "证件类型",notes = "身份证、驾驶证、军官证、营业执照")
    String cardType;

    @ApiModelProperty("证件号")
    String cardNumber;

    @ApiModelProperty("联系人")
    String contacts;

    @ApiModelProperty("联系人电话")
    String contactsPhone;

    @ApiModelProperty("经营范围")
    String scope;

    @ApiModelProperty("邮政编码")
    String postalCode;

    @ApiModelProperty("中文地址")
    String addr;


    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry mapAddr;

    @ApiModelProperty("地图地址")
    @Transient
    private String wkt;

    @ApiModelProperty("身份证拍照文件id(多文件以分号隔开)")
    @Lob
    private String cardFileStore;

    @ApiModelProperty("毕业证书文件id(多文件以分号隔开)")
    @Lob
    private String graduationFileStore;

    @ApiModelProperty("技能证书文件id(多文件以分号隔开)")
    @Lob
    private String skillFileStore;

    @ApiModelProperty("营业执照文件id(多文件以分号隔开)")
    @Lob
    private String businessFileStore;

    @ApiModelProperty("审核是否通过")
    Boolean auditPass;

    @ApiModelProperty("审核时间")
    Date auditTime;

    @ApiModelProperty("审核意见")
    String auditReason;

    @ApiModelProperty("审核人")
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "auditUser_id")
    @Fetch(FetchMode.JOIN)
    SysUser auditUser;

    /**
     * 申请时候用户的openId
     */
    @JsonIgnore
    String openId;


    @ApiModelProperty("最终创建的用户")
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "targetUser_id")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    SysUser targetUser;

    /**
     * 回显给前端时使用
     */
    public String getWkt() {
        if (this.mapAddr != null) {
            return this.mapAddr.toText();
        }
        return wkt;
    }

    /**
     * 转换wkt
     */
    @PrePersist
    public void wkcCovert() throws ParseException {
        mapAddr = GeometryUtil.transWKT(wkt);
    }


}
