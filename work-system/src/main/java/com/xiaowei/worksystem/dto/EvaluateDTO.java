package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModelProperty;

public class EvaluateDTO {
    /**
     * 评价等级
     */
    @ApiModelProperty(value = "评价等级")
    private String level;
    /**
     * 使用评价
     */
    @ApiModelProperty(value = "使用评价")
    private String useEvaluate;
    /**
     * 态度评价
     */
    @ApiModelProperty(value = "态度评价")
    private String mannerEvaluate;
    /**
     * 技能评价
     */
    @ApiModelProperty(value = "技能评价")
    private String skillEvaluate;
    /**
     * 速度评价
     */
    @ApiModelProperty(value = "速度评价")
    private String speedEvaluate;

    /**
     * 反馈意见
     */
    @ApiModelProperty(value = "反馈意见")
    private String option;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUseEvaluate() {
        return useEvaluate;
    }

    public void setUseEvaluate(String useEvaluate) {
        this.useEvaluate = useEvaluate;
    }

    public String getMannerEvaluate() {
        return mannerEvaluate;
    }

    public void setMannerEvaluate(String mannerEvaluate) {
        this.mannerEvaluate = mannerEvaluate;
    }

    public String getSkillEvaluate() {
        return skillEvaluate;
    }

    public void setSkillEvaluate(String skillEvaluate) {
        this.skillEvaluate = skillEvaluate;
    }

    public String getSpeedEvaluate() {
        return speedEvaluate;
    }

    public void setSpeedEvaluate(String speedEvaluate) {
        this.speedEvaluate = speedEvaluate;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
