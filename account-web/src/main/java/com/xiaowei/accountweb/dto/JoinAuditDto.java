package com.xiaowei.accountweb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("审核信息")
@Data
public class JoinAuditDto {
    @ApiModelProperty("待审核的id")
    String id;

    @ApiModelProperty("审核意见")
    String reason;

    @ApiModelProperty("是否通过")
    Boolean pass = true;

    @ApiModelProperty(value = "入驻类型",notes = "默认使用默认入驻类型，此时使用申请时候选择的入驻方式进行")
    JoinType joinType = JoinType.Default;

    @ApiModelProperty("用户入驻申请的附属信息")
    EngineerJoinApply engineerJoinApply;

    @ApiModelProperty("公司入驻申请的附属信息")
    CompanyJoinApply companyJoinApply;

    @Data
    public static class EngineerJoinApply{

        @ApiModelProperty(value = "为用户分配的部门id",required = true)
        String departmentId;

        @ApiModelProperty(value = "为用户分配的角色id",required = true)
        List<String> roleIds;
    }

    /**
     * 入驻类型
     */
    public enum JoinType {
        Default,Engineer,Company
    }

    @Data
    public static class CompanyJoinApply{

        @ApiModelProperty(value = "入驻的公司名称",required = true)
        String companyName;

    }
}
