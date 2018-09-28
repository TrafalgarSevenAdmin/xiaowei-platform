package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;
import com.xiaowei.worksystem.entity.flow.WorkFlowNode;
import com.xiaowei.worksystem.repository.flow.WorkFlowItemRepository;
import com.xiaowei.worksystem.repository.flow.WorkFlowNodeRepository;
import com.xiaowei.worksystem.service.IWorkFlowNodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class WorkFlowNodeServiceImpl extends BaseServiceImpl<WorkFlowNode> implements IWorkFlowNodeService {

    @Autowired
    private WorkFlowNodeRepository workFlowNodeRepository;
    @Autowired
    private WorkFlowItemRepository workFlowItemRepository;

    public WorkFlowNodeServiceImpl(@Qualifier("workFlowNodeRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public WorkFlowNode updateWorkFlowNode(WorkFlowNode workFlowNode) {
        List<WorkFlowItem> workFlowItems =  workFlowItemRepository.findByCode(workFlowNode.getCode());
        if(CollectionUtils.isNotEmpty(workFlowItems)){
            //更新流程模板明细
            workFlowItems.stream().forEach(workFlowItem -> {
                transWorkFlowItemByNode(workFlowItem,workFlowNode);
                workFlowItemRepository.save(workFlowItem);
            });
        }
        //更新节点
        return workFlowNodeRepository.save(workFlowNode);
    }

    private void transWorkFlowItemByNode(WorkFlowItem workFlowItem, WorkFlowNode workFlowNode) {
        workFlowItem.setAudit(workFlowNode.getAudit());
        workFlowItem.setCharge(workFlowNode.getCharge());
        workFlowItem.setInsidePrice(workFlowNode.getInsidePrice());
        workFlowItem.setOutsidePrice(workFlowNode.getOutsidePrice());
        workFlowItem.setOutToll(workFlowNode.getOutToll());
        workFlowItem.setPredictTime(workFlowNode.getPredictTime());
        workFlowItem.setServiceIntro(workFlowNode.getServiceIntro());
        workFlowItem.setServiceType(workFlowNode.getServiceType());
        workFlowItem.setStandard(workFlowNode.getStandard());
        workFlowItem.setVersion(workFlowNode.getVersion());
        workFlowItem.setToll(workFlowNode.getToll());
        workFlowItem.setStandardFileStore(workFlowNode.getStandardFileStore());
    }
}
