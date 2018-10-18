package com.xiaowei.account.multi.entity;


import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import static com.xiaowei.account.multi.consts.MultiConsts.MultiTenancyFilterName;


/**
 * 多租户实例
 */
@MappedSuperclass
@FilterDef(name = MultiTenancyFilterName, parameters = {
        @ParamDef(name = "tenancyId", type = "string")
})
@Filter(name = MultiTenancyFilterName, condition = "tenancy_id=:tenancyId")
@Data
public class MultiBaseEntity extends BaseEntity {

    /**
     * 租户id
     */
    @Column(updatable = false)
    private String tenancyId;

    @PrePersist
    public void onTenancyId() {
        LoginUserBean loginUserOrNull = LoginUserUtils.getLoginUserOrNull();
        tenancyId = loginUserOrNull != null ? loginUserOrNull.getTenancyId() : null;
    }

}
