package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.status.WorkpieceStoreType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

public class WorkpieceDTO {

    /**
     * 工程师添加坏件判断
     */
    public interface EngineerAddBadWorkpiece {}

    /**
     * 工件编码
     */
    @ApiModelProperty(value = "工件编码")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "工件编码必填!")
    private String code;

    /**
     * 工件名称
     */
    @ApiModelProperty(value = "工件名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "工件名称必填!")
    private Equipment name;

    /**
     * 所属厂库类型，0个人厂库，1总库
     */
    @ApiModelProperty(value = "所属厂库类型，0个人厂库，1总库")
    private Integer storeType = WorkpieceStoreType.USER.getStatus();

    /**
     * 好件数量
     */
    @ApiModelProperty(value = "好件数量")
    @Null(groups = EngineerAddBadWorkpiece.class,message = "工程师不能添加好件")
    private Integer FineTotal = 0;

    /**
     * 坏件数量
     */
    @ApiModelProperty(value = "坏件数量")
    private Integer BadTotal;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Equipment getName() {
        return name;
    }

    public void setName(Equipment name) {
        this.name = name;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getFineTotal() {
        return FineTotal;
    }

    public void setFineTotal(Integer fineTotal) {
        FineTotal = fineTotal;
    }

    public Integer getBadTotal() {
        return BadTotal;
    }

    public void setBadTotal(Integer badTotal) {
        BadTotal = badTotal;
    }
}
