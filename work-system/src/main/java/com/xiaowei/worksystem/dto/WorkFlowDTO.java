package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WorkFlowDTO {
    public interface UpdateStatus{}
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "模板名称必填!")
    private String workFlowName;
    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "模板类型必填!")
    private String type;
    /**
     * 模板简介
     */
    @ApiModelProperty(value = "模板简介")
    private String intro;
    /**
     * 模板下的流程明细
     */
    @ApiModelProperty(value = "模板下的流程明细")
    private List<WorkFlowItem> workFlowItems;

    @ApiModelProperty(value = "模板状态:0代表正常,1代表禁用")
    @NotNull(groups = {WorkFlowDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {WorkFlowDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;
}
