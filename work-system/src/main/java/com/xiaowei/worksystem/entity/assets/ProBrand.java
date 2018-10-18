package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品品牌
 */
@Data
@Entity
@Table(name = "W_PRO_BRAND")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class ProBrand extends MultiBaseEntity {
    /**
     * 品牌编码
     * 该品牌唯一对应的一个数字编码
     */
    public String code;

    /**
     * 品牌名称
     * 该品牌对应的名称
     */
    public String name;
}
