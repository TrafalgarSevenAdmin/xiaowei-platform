package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 入库单明细
 */
@Entity
@Table(name = "W_INV_ORDER_IN_ITEM")
public class InvOrderInItem extends BaseEntity {

    // TODO: 2018/7/6 0006 流程编码 模板编码

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
     * 物料数量
     * 该业务单对应的物料数量
     */
    public Integer number;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
