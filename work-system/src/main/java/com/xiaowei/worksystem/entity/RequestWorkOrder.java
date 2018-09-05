package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.entity.customer.Customer;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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
    private String serviceType;
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
    private String repairFileStore;

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

    @Transient
    @JsonIgnore
    private List<Map<String, String>> repairFileStorePath;

    public List<Map<String, String>> getRepairFileStorePath() {
        return UploadConfigUtils.transIdsToPath(this.repairFileStore);
    }

}
