package com.xiaowei.worksystem.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 工单实体
 */
@Entity
@Table(name = "W_WORKORDER")
@SQLDelete(sql = "update w_workorder set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class WorkOrder extends BaseEntity {

    /**
     * 工单编号
     */
    @Column(unique = true)
    private String code;
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
     * 工单类型
     */
    private String workOrderType;

    /**
     * 针对后台处理人员状态
     */
    private Integer systemStatus;
    /**
     * 针对用户状态
     */
    private Integer userStatus;
    /**
     * 创建方式
     */
    private Integer createdType;
    /**
     * 申请处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "proposer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser proposer;
    /**
     * 后台处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "backgrounder_id")
    @Fetch(FetchMode.JOIN)
    private SysUser backgrounder;
    /**
     * 处理工程师
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "engineer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser engineer;

    /**
     * 当前处理步骤
     */
    private Integer currentOrderNumber;

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public Integer getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    public void setCurrentOrderNumber(Integer currentOrderNumber) {
        this.currentOrderNumber = currentOrderNumber;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

    /**
     * 用户评价
     */
    @OneToOne(targetEntity = Evaluate.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_id")
    @Fetch(FetchMode.JOIN)
    private Evaluate evaluate;

    /**
     * 工程师处理工单附表
     */
    @OneToOne(targetEntity = EngineerWork.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "engineerWork_id")
    @Fetch(FetchMode.JOIN)
    private EngineerWork engineerWork;

    /**
     * 报修文件id(多文件以分号隔开)
     */
    private String repairFileStore;

    public String getRepairFileStore() {
        return UploadConfigUtils.transIdsToPath(this.repairFileStore);
    }

    public void setRepairFileStore(String repairFileStore) {
        this.repairFileStore = repairFileStore;
    }

    public EngineerWork getEngineerWork() {
        return engineerWork;
    }

    public void setEngineerWork(EngineerWork engineerWork) {
        this.engineerWork = engineerWork;
    }

    public Integer getCreatedType() {
        return createdType;
    }

    public void setCreatedType(Integer createdType) {
        this.createdType = createdType;
    }

    public SysUser getProposer() {
        return proposer;
    }

    public void setProposer(SysUser proposer) {
        this.proposer = proposer;
    }

    public SysUser getBackgrounder() {
        return backgrounder;
    }

    public void setBackgrounder(SysUser backgrounder) {
        this.backgrounder = backgrounder;
    }

    public SysUser getEngineer() {
        return engineer;
    }

    public void setEngineer(SysUser engineer) {
        this.engineer = engineer;
    }

    public Integer getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(Integer systemStatus) {
        this.systemStatus = systemStatus;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
