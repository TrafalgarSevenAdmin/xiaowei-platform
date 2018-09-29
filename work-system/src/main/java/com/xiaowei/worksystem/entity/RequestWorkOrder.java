package com.xiaowei.worksystem.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.entity.customer.Customer;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * 工单请求
 */
@Entity
@Table(name = "W_REQUESTWORKORDER")
@Data
public class RequestWorkOrder extends BaseEntity {
    /**
     * 所属设备
     */
    @ManyToOne(targetEntity = Equipment.class)
    @JoinColumn(name = "equipment_id")
    @Fetch(FetchMode.JOIN)
    private Equipment equipment;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 故障描述
     */
    private String errorDescription;
    /**
     * 服务类型
     */
    @ManyToOne(targetEntity = WorkOrderType.class)
    @JoinColumn(name = "type_id")
    @Fetch(FetchMode.JOIN)
    private WorkOrderType workOrderType;
    /**
     * 申请处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "proposer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser proposer;

    /**
     * 报修文件id(多文件以分号隔开)
     */
    @Lob
    private String repairFileStore;

    /**
     * 报修音频id(多文件以分号隔开)
     */
    @Lob
    private String repairVoice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 服务对象
     */
    @OneToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    @Fetch(FetchMode.JOIN)
    private Customer customer;

    /**
     * 区县
     */
    private String county;


}
