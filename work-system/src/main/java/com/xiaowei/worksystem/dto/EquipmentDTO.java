package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class EquipmentDTO {

    @ApiModelProperty(value = "设备名称")
    @NotBlank(groups = {V.Update.class},message = "设备名称必填!")
    private String equipmentName;

    @ApiModelProperty(value = "设备编号")
    @NotBlank(groups = {V.Update.class},message = "设备编号必填!")
    private String code;

    @ApiModelProperty(value = "设备类型")
    @NotBlank(groups = {V.Update.class},message = "设备类型必填!")
    private String type;

    @ApiModelProperty(value = "设备地址")
    @NotBlank(groups = {V.Update.class},message = "设备地址必填!")
    private String address;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
