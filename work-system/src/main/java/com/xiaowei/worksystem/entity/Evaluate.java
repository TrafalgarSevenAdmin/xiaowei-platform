package com.xiaowei.worksystem.entity;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评价实体
 */
@Entity
@Table(name = "W_EVALUATE")
@SQLDelete(sql = "update w_evaluate set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Evaluate extends MultiBaseEntity {
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
