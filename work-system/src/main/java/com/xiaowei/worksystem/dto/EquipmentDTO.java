package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class EquipmentDTO {

    @ApiModelProperty(value = "设备名称")
    @NotBlank(groups = {V.Update.class},message = "设备名称必填!")
    private String equipmentName;

    @ApiModelProperty(value = "设备编号")
    @NotBlank(groups = {V.Update.class},message = "设备编号必填!")
    private String equipmentNo;

    @ApiModelProperty(value = "设备类型")
    @NotBlank(groups = {V.Update.class},message = "设备类型必填!")
    private String type;

    @ApiModelProperty(value = "设备地址")
    @NotBlank(groups = {V.Update.class},message = "设备地址必填!")
    private String address;

    /**
     * 类型
     */
    String equipmentType;

    /**
     * 品牌
     */
    String equipmentBrand;

    /**
     * 型号
     */
    String equipmentModel;

    /**
     * 生产日期
     */
    Date madeDate;

    /**
     * 生产批次
     */
    String batchNo;

    /**
     * 安装日期
     */
    Date installDate;

    /**
     * 维保状态
     */
    String warrantyStatus;

    /**
     * 服务合同号
     */
    String contractNo;

    /**
     * 网点名称
     */
    String branchName;

    /**
     * 客户设备编号
     */
    String customerEquipmentNo;

    /**
     * 状态
     */
    String status;

    /**
     * 安装方式 大堂、穿墙
     */
    String installType;

    /**
     * IP地址
     */
    String ip;

    /**
     * 经度
     */
    Double longitude;

    /**
     * 纬度
     */
    Double latitude;

    /**
     * 维保到期日期
     */
    String warrantyTime;

    /**
     * 安装位置(离行、在行)
     */
    String locationType;

    /**
     * 机芯类型
     */
    String coreType;

    WorkFlow workFlow;

//    /**
//     * 操作系统名称
//     */
//    String osName;
//
//    /**
//     * 操作系统版本
//     */
//    String osVersion;
//
//    /**
//     * ATMC名称
//     */
//    String atmcName;
//
//    /**
//     * ATMC版本
//     */
//    String atmcVersion;
//
//    /**
//     * SP名称
//     */
//    String spName;
//
//    /**
//     * SP版本
//     */
//    String spVersion;

    /**
     * 所属服务对象
     */
    Customer customer;

}
