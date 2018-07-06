package com.xiaowei.worksystem.entity.assets;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品品牌
 */
@Data
@Entity
@Table(name = "W_PRO_BRAND")
public class ProBrand extends BaseEntity {
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
