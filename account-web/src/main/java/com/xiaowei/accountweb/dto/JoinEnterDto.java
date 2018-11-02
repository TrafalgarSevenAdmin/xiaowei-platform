package com.xiaowei.accountweb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("入驻申请单")
@Data
public class JoinEnterDto {
    @ApiModelProperty("用户名")
    String userName;

    @ApiModelProperty("手机号")
    String mobilePhone;

    @ApiModelProperty("邮箱号")
    String email;

    @ApiModelProperty(value = "用户类型",notes = "个人、公司、个体工商户、工作室、项目组")
    String userType;

    @ApiModelProperty(value = "要加入的公司Id")
    String tenementId;

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

    @ApiModelProperty("地图地址")
    private String wkt;

    @ApiModelProperty("拍照文件id(多文件以分号隔开)")
    private String cardFileStore;

    @ApiModelProperty("毕业证书文件id(多文件以分号隔开)")
    private String graduationFileStore;

    @ApiModelProperty("营业执照文件id(多文件以分号隔开)")
    private String businessFileStore;
}
