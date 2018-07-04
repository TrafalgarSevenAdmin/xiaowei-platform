package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModelProperty;

public class EquipmentDTO {

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(value = "设备编号")
    private String code;

    @ApiModelProperty(value = "设备类型")
    private String type;

    @ApiModelProperty(value = "设备地址")
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
