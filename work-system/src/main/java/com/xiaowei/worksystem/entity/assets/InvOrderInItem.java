package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 入库单明细
 */
@Data
@Entity
@Table(name = "W_INV_ORDER_IN_ITEM")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class InvOrderInItem extends MultiBaseEntity {

    /**
     * 入库单编码
     * 该业务单对应的唯一一个编码标识
     */
    public String code;

    /**
     * 物料信息
     */
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    public Product product;

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

}
