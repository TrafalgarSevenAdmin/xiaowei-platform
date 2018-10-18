package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.worksystem.status.InvOrderInType;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 入库单
 */
@Data
@Entity
@Table(name = "W_INV_ORDER_IN")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class InvOrderIn extends MultiBaseEntity {


    /**
     * 出库单编码
     * 该业务单对应的唯一一个编码标识
     */
    public String code;

    /**
     * 相关单据编码
     * 该业务单对应的相关业务单据编码，如：工单编码，销售订单编码等
     */
    public String relevantCode;

    /**
     * 部门名称
     * 该业务单对应的部门名称
     */
    public String departmentName;

    /**
     * 调入仓库信息
     */
    @ManyToOne(targetEntity = Warehouse.class)
    @JoinColumn(name = "in_warehouse_id")
    @Fetch(FetchMode.JOIN)
    public Warehouse inWarehouse;

    /**
     * 调出仓库信息
     */
    @ManyToOne(targetEntity = Warehouse.class)
    @JoinColumn(name = "out_warehouse_id")
    @Fetch(FetchMode.JOIN)
    public Warehouse outWarehouse;

    /**
     * 入库类型
     * 该业务单对应的入库类型：
     * 1：采购入库，2：调拨入库，3：库存调整盘盈，4：设备维修（坏件退回），5：其它
     * @see InvOrderInType
     */
    public Integer type;

    /**
     * 申请人
     * 该业务单对应的申请人姓名
     */
    public String applyUserName;

    /**
     * 申请原因
     * 该业务单对应的申请原因
     */
    public String applyReason;

    /**
     * 入库单明细
     */
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "code",referencedColumnName = "code")
    public List<InvOrderInItem> invOrderInItems;

    /**
     * 审核时间
     */
    Date auditTime;

    /**
     * 审核意见
     */
    String auditReason;

    /**
     * 审核人
     */
    String auditUserId;

    /**
     * 审核人名称
     */
    String auditUserName;


    // TODO: 2018/7/6 0006 附加字段
    // 部门审批	该业务单对应的部门经理审批意见
    //运营审批	该业务单对应的财务审批意见
    //公司审批	该业务单对应的公司审批意见
    //公司审批日期	该业务单对应的公司审批通过日期
}
