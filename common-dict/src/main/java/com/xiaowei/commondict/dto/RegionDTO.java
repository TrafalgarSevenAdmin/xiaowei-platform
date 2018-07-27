package com.xiaowei.commondict.dto;

import com.xiaowei.core.validate.V;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
* @author yaohuaiying
* @Date 2017-09-01  13:43
* @Description行政区域视图
* @Version 1.0
*/
@Data
public class RegionDTO {

   private String code;

   @Size(min = 1,max = 100,groups = {V.Insert.class,V.Update.class},message = "行政区域名称[1,100]")
   @NotBlank(groups = {V.Insert.class,V.Update.class},message = "行政区域名称不能为空")
   private String name;

   @Size(min = 2,max = 14,groups = {V.Insert.class,V.Update.class},message = "行政区域名称[1,100]")
   @NotBlank(groups = {V.Insert.class,V.Update.class},message = "行政区域代码不能为空")
   private String shortCode;

   private String parentShortCode;

   private String mergerName;

   private Integer level;

}
