package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.status.ProductTagType;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 产品目录
 */
@Data
@Entity
@Table(name = "W_PRODUCT")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class Product extends BaseEntity {
    /**
     * 产品编码
     * 该产品唯一对应的一个数字编码标识
     */
    public String code;

    /**
     * 产品名称
     * 该物料的名称
     */
    public String name;

    /**
     * 产品品牌
     * 该商品对应的品牌
     */
    @ManyToOne(targetEntity = ProBrand.class)
    @JoinColumn(name = "pro_brand_id")
    @Fetch(FetchMode.JOIN)
    public ProBrand proBrand;

    /**
     * 产品分类
     * 该产品对应的分类
     */
    @ManyToOne(targetEntity = ProClass.class)
    @JoinColumn(name = "pro_class_code",referencedColumnName = "code")
    @Fetch(FetchMode.JOIN)
    public ProClass proClass;

    /**
     * 产品型号
     */
    public String model;

    /**
     * 计量单位
     */
    public String unit;

    /**
     * 安全库存
     * 库存产品的安全数量
     */
    public Integer saveNumbe;

    /**
     * 备注
     */
    public String note;

    // TODO 附加字段
    // 安全库存	该产品对应的安全库存
    //    //供应商	该产品对应的供应商
    //    //国际条码	该商品对应的国际条码（GTIN码，前缀为690-699的为国内条码）
    //    //计量单位	该产品对应的计量单位，如千克，个，套等
    //    //合同进价	该产品对应的供应商合同价格（价格审批和采购合同审批流程确定的价格）
    //    //合同售价	该产品对应的合同售价（正常售价）
    //    //产地	该商品的原产地
    //    //生产日期	该商品的生产日期
    //    //保质期	该商品的保质期，单位为天
    //    //产品状态	状态为1表示正常，2：停止订货，3：停止销售
    //    //启用日期	产品建档日期

}
