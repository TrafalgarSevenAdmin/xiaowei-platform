package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 产品库存
 */
@Data
@Entity
@Table(name = "W_INVENTORY")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class Inventory  extends BaseEntity {
    // TODO: 2018/7/6 0006 租户编码

    /**
     * 产品信息
     */
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    public Product product;

    /**
     * 所属仓库信息
     */
    @ManyToOne(targetEntity = Warehouse.class)
    @JoinColumn(name = "warehouse_id")
    @Fetch(FetchMode.JOIN)
    public Warehouse warehouse;

    /**
     * 库存数量-好件
     * 库存产品数量-好件
     */
    public Integer fineNumber;

    /**
     * 库存数量-坏件
     * 库存产品数量-坏件
     */
    public Integer badNumber;

    /**
     * 安全库存
     * 库存产品的安全数量
     */
    public Integer saveNumber = 10;

    /**
     * 在途数量
     */
    public Integer passageNumber = 0;

}
