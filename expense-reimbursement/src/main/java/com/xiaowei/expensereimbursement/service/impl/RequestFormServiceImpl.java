package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.RequestForm;
import com.xiaowei.expensereimbursement.entity.RequestFormItem;
import com.xiaowei.expensereimbursement.entity.WorkOrderSelect;
import com.xiaowei.expensereimbursement.repository.RequestFormItemRepository;
import com.xiaowei.expensereimbursement.repository.RequestFormRepository;
import com.xiaowei.expensereimbursement.repository.WorkOrderSelectRepository;
import com.xiaowei.expensereimbursement.service.IRequestFormService;
import com.xiaowei.expensereimbursement.status.RequestFormItemStatus;
import com.xiaowei.expensereimbursement.status.RequestFormStatus;
import com.xiaowei.expensereimbursement.utils.ExpenseFormUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;


@Service
public class RequestFormServiceImpl extends BaseServiceImpl<RequestForm> implements IRequestFormService {

    @Autowired
    private RequestFormRepository requestFormRepository;
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    @Autowired
    private WorkOrderSelectRepository workOrderSelectRepository;
    @Autowired
    private RequestFormItemRepository requestFormItemRepository;

    public RequestFormServiceImpl(@Qualifier("requestFormRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public RequestForm saveRequestForm(RequestForm requestForm) {
        //判定参数是否合规
        judgeAttribute(requestForm, JudgeType.INSERT);
        requestFormRepository.save(requestForm);

        //判断所有明细的费用科目是否唯一以及合计金额是否正确
        //保存科目明细
        judgeItemIsUniqueAndAmount(requestForm);
        return requestForm;
    }

    private void judgeItemIsUniqueAndAmount(RequestForm requestForm) {
        double total = 0;
        Set<String> subjectCodes = new HashSet<>();
        final List<RequestFormItem> requestFormItems = requestForm.getRequestFormItems();
        for (RequestFormItem requestFormItem : requestFormItems) {
            total = total + requestFormItem.getFillFigure();
            if (subjectCodes.contains(requestFormItem.getSubjectCode())) {
                throw new BusinessException("报销费用科目重复!");
            } else {
                subjectCodes.add(requestFormItem.getSubjectCode());
            }
            requestFormItem.setCreatedTime(new Date());
            if (RequestFormStatus.DRAFT.getStatus().equals(requestForm.getStatus())) {
                //如果是草稿,则明细也存草稿
                requestFormItem.setStatus(RequestFormItemStatus.DRAFT.getStatus());
            } else {
                requestFormItem.setStatus(RequestFormItemStatus.NORMAL.getStatus());
            }
            requestFormItem.setRequestForm(requestForm);
            requestFormItemRepository.save(requestFormItem);
        }
        //判断金额
        if (total != requestForm.getFillAmount()) {
            throw new BusinessException("填报总金额有误!");
        }
    }

    private void judgeAttribute(RequestForm requestForm, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            requestForm.setId(null);
            requestForm.setTurnDownCount(0);//初始化驳回次数
            requestForm.setCode(getCurrentDayMaxCode());
            requestForm.setCreatedTime(new Date());
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String requestFormId = requestForm.getId();
            EmptyUtils.assertString(requestFormId, "没有传入对象id");
            Optional<RequestForm> optional = requestFormRepository.findById(requestFormId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
            final RequestForm one = optional.get();
            if (!ArrayUtils.contains(ExpenseFormUtils.CANUPDATE, one.getStatus())) {
                throw new BusinessException("申请单当前不允许修改");
            }

            //设置无法修改的属性
            requestForm.setTurnDownCount(one.getTurnDownCount());//驳回次数无法修改
        }

        //验证所属工单
        judgeWorkOrder(requestForm);

    }

    private void judgeWorkOrder(RequestForm requestForm) {
        final String workOrderCode = requestForm.getWorkOrderCode();
        EmptyUtils.assertString(workOrderCode, "没有传入所属工单编号");
        final WorkOrderSelect workOrderSelect = workOrderSelectRepository.findByCode(workOrderCode);
        EmptyUtils.assertObject(workOrderSelect, "没有查询到所属工单");
        //如果工单已归档,则抛出异常
        if (workOrderSelect.getSystemStatus() != 10) {
            throw new BusinessException("该工单已经关闭!");
        }
        if (workOrderSelect.getSystemStatus() != 7) {
            throw new BusinessException("该工单状态无法创建申请单!");
        }
        //查询是否有其他申请单
        if(CollectionUtils.isNotEmpty(requestFormRepository.findByWorkOrderCode(workOrderCode))){
            throw new BusinessException("该工单已有申请单!");
        }
    }

    private String getCurrentDayMaxCode() {
        String code = "FYSQ" + DateUtils.getCurrentDate();
        String incr;
        ShardedJedis resource = shardedJedisPool.getResource();
        if (!resource.exists(code)) {
            resource.setex(code, 86400, "1");//24小时有效期
            incr = 1 + "";
        } else {
            if (999 == Long.valueOf(resource.get(code))) {
                throw new BusinessException("编码数量超出范围");
            } else {
                incr = resource.incr(code) + "";
            }
        }
        int len = 4 - incr.length();
        for (int i = 0; i < len; i++) {
            incr = "0" + incr;
        }
        return code + incr;
    }

    @Override
    @Transactional
    public RequestForm updateRequestForm(RequestForm requestForm) {
        //判定参数是否合规
        judgeAttribute(requestForm, JudgeType.UPDATE);
        requestFormRepository.save(requestForm);
        //判断所有明细的费用科目是否唯一以及合计金额是否正确
        requestFormItemRepository.deleteByRequestFormId(requestForm.getId());
        //先删除明细,再保存明细
        judgeItemIsUniqueAndAmount(requestForm);
        return requestForm;
    }

    /**
     * 费用申请审核
     * @param requestForm
     * @param audit
     * @return
     */
    @Override
    @Transactional
    public RequestForm audit(RequestForm requestForm, Boolean audit) {
        final String requestFormId = requestForm.getId();
        EmptyUtils.assertString(requestFormId, "没有传入对象id");
        Optional<RequestForm> optional = requestFormRepository.findById(requestFormId);
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        RequestForm one = optional.get();
        //判断状态
        if (!RequestFormStatus.PREAUDIT.getStatus().equals(one.getStatus()) &&
                !RequestFormStatus.TURNDOWN.getStatus().equals(one.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        //判断审核总计金额是否合规
        judgeTrialAmount(requestForm, one);
        //判断审核申请单明细
        double totalAmount = judgeItem(requestForm, one);
        if (totalAmount != requestForm.getTrialAmount()) {
            //如果明细累加的审核总计金额与前端传送过来的总计金额不一致,则抛出异常
            throw new BusinessException("审核总计金额有误!");
        }

        //判断最终审核人是否在审核人里面
        judgeAudit(requestForm, one);
        one.setAuditTime(new Date());//审核时间
        if (audit) {//是否驳回
            one.setStatus(RequestFormStatus.AUDIT.getStatus());
        } else {
            //设置驳回次数
            one.setTurnDownCount(one.getTurnDownCount() == null ? 1 : (one.getTurnDownCount() + 1));
            one.setStatus(RequestFormStatus.TURNDOWN.getStatus());
        }
        one.setOption(requestForm.getOption());//审核意见
        return requestFormRepository.save(one);
    }

    private void judgeAudit(RequestForm requestForm, RequestForm one) {
        EmptyUtils.assertObject(requestForm.getAudit(), "没有传入最终审核人");
        Long count = requestFormRepository.findCountTrial(requestForm.getId(), requestForm.getAudit().getId());
        if (count == 0) {
            throw new BusinessException("该申请单没有该审核人");
        }
        one.setAudit(requestForm.getAudit());//设置最终审核人
    }

    private double judgeItem(RequestForm requestForm, RequestForm one) {
        double totalAmount = 0;
        List<RequestFormItem> requestFormItems = requestForm.getRequestFormItems();
        List<RequestFormItem> oneRequestFormItems = one.getRequestFormItems();
        for (RequestFormItem oneItem : oneRequestFormItems) {
            Optional<RequestFormItem> optional = requestFormItems.stream().filter(requestFormItem -> requestFormItem.getId().equals(oneItem.getId())).findAny();
            if (!optional.isPresent()) {
                throw new BusinessException("费用明细有误");
            }
            final Double figure = optional.get().getFigure();
            EmptyUtils.assertObject(figure, "没有明细初审金额");
            if (figure > oneItem.getFillFigure()) {
                throw new BusinessException("费用明细初审金额大于填报金额");
            }
            oneItem.setFigure(figure);
            Integer status = optional.get().getStatus();
            EmptyUtils.assertObject(status, "没有传入明细状态");
            oneItem.setStatus(status);
            if (!RequestFormItemStatus.REFUSE.getStatus().equals(status)) {//非不予报销
                totalAmount = totalAmount + figure;
            }
            requestFormItemRepository.save(oneItem);
        }
        return totalAmount;
    }

    /**
     * 判断审核总计金额是否合规
     * @param requestForm
     * @param one
     */
    private void judgeTrialAmount(RequestForm requestForm, RequestForm one) {
        final Double trialAmount = requestForm.getTrialAmount();
        EmptyUtils.assertObject(trialAmount, "没有传入审核总计金额");
        if (trialAmount > one.getFillAmount()) {
            //不允许审核总计金额大于填报总计金额
            throw new BusinessException("不允许审核总计金额大于填报总计金额");
        } else {
            one.setTrialAmount(trialAmount);//设置审核总金额
        }
    }
}
