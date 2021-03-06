package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;
import com.xiaowei.worksystem.repository.flow.WorkFlowItemRepository;
import com.xiaowei.worksystem.repository.flow.WorkFlowRepository;
import com.xiaowei.worksystem.service.IWorkFlowService;
import com.xiaowei.worksystem.status.WorkFlowStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class WorkFlowServiceImpl extends BaseServiceImpl<WorkFlow> implements IWorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private WorkFlowItemRepository workFlowItemRepository;

    public WorkFlowServiceImpl(@Qualifier("workFlowRepository") WorkFlowRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public WorkFlow saveWorkFlow(WorkFlow workFlow) {
        //判定参数是否合规
        judgeAttribute(workFlow, JudgeType.INSERT);
        workFlowRepository.save(workFlow);
        saveItem(workFlow);//保存流程明细
        return workFlow;
    }

    private void judgeAttribute(WorkFlow workFlow, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            workFlow.setId(null);
            workFlow.setCode(StringPYUtils.getSpellCode(workFlow.getWorkFlowName() + workFlow.getType()));
            workFlow.setCreatedTime(new Date());
            workFlow.setStatus(WorkFlowStatus.NORMAL.getStatus());
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String workFlowId = workFlow.getId();
            EmptyUtils.assertString(workFlowId, "没有传入对象id");
            Optional<WorkFlow> one = workFlowRepository.findById(workFlowId);
            EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        }
    }

    /**
     * 保存流程明细
     *
     * @param workFlow
     */
    private void saveItem(WorkFlow workFlow) {
        List<WorkFlowItem> workFlowItems = workFlow.getWorkFlowItems();
        if (CollectionUtils.isEmpty(workFlowItems)) {
            return;
        }
        for (int i = 0; i < workFlowItems.size(); i++) {
            WorkFlowItem workFlowItem = workFlowItems.get(i);
            workFlowItem.setId(null);
            workFlowItem.setCreatedTime(null);//创建时间
            workFlowItem.setOrderNumber(i + 1);//设置排序号
            workFlowItem.setWorkFlowId(workFlow.getId());//设置所属流程模板id
            workFlowItemRepository.save(workFlowItem);
        }
    }

    @Override
    @Transactional
    public WorkFlow updateWorkFlow(WorkFlow workFlow) {
        //判定参数是否合规
        judgeAttribute(workFlow, JudgeType.UPDATE);
        workFlowRepository.save(workFlow);
        workFlowItemRepository.deleteByWorkFlowId(workFlow.getId());//删除模板下的流程明细
        saveItem(workFlow);//重新保存流程明细
        return workFlow;
    }

    @Override
    @Transactional
    public void deleteWorkFlow(String workFlowId) {
        EmptyUtils.assertString(workFlowId, "没有传入对象id");
        Optional<WorkFlow> one = workFlowRepository.findById(workFlowId);
        EmptyUtils.assertOptional(one, "没有查询到需要删除的对象");
        WorkFlow workFlow = one.get();
        workFlowItemRepository.deleteByWorkFlowId(workFlowId);//删除模板下的流程明细
        workFlowRepository.delete(workFlow);//删除模板
    }

    @Override
    @Transactional
    public WorkFlow updateStatus(WorkFlow workFlow) {
        String workFlowId = workFlow.getId();
        if (StringUtils.isEmpty(workFlowId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Optional<WorkFlow> optional = workFlowRepository.findById(workFlowId);
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        WorkFlow one = optional.get();
        one.setStatus(workFlow.getStatus());
        workFlowRepository.save(one);
        return one;
    }
}
