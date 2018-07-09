package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Data
public class InventoryChageDTO {

    @ApiModelProperty(value = "更改列表")
    private List<Chage> chages = new ArrayList<>();


    @ApiModel("库存更改")
    @Data
    public class Chage {

        @ApiModelProperty(value = "库存id")
        private String id;

        @ApiModelProperty("好件使用数量，只能是正数")
        @Min(message = "只能是正数",value = 0)
        private Integer fineChage;

        @ApiModelProperty("坏件回收数量，只能是正数")
        @Min(message = "只能是正数",value = 0)
        private Integer badChage;
    }

}
