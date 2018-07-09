package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;

import javax.persistence.*;

/**
 * 评价实体
 */
@Entity
@Table(name = "W_EVALUATE")
public class Evaluate extends BaseEntity{
    /**
     * 评价等级
     */
    private String level;
    /**
     * 使用评价
     */
    private String useEvaluate;
    /**
     * 态度评价
     */
    private String mannerEvaluate;
    /**
     * 技能评价
     */
    private String skillEvaluate;
    /**
     * 速度评价
     */
    private String speedEvaluate;

    /**
     * 反馈意见
     */
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

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
}
