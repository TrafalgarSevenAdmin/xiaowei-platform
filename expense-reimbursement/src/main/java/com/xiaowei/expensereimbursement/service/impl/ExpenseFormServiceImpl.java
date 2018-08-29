package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;
import com.xiaowei.expensereimbursement.entity.ExpenseFormItem;
import com.xiaowei.expensereimbursement.entity.WorkOrderSelect;
import com.xiaowei.expensereimbursement.repository.ExpenseFormItemRepository;
import com.xiaowei.expensereimbursement.repository.ExpenseFormRepository;
import com.xiaowei.expensereimbursement.repository.RequestFormRepository;
import com.xiaowei.expensereimbursement.repository.WorkOrderSelectRepository;
import com.xiaowei.expensereimbursement.service.IExpenseFormService;
import com.xiaowei.expensereimbursement.status.ExpenseFormItemStatus;
import com.xiaowei.expensereimbursement.status.ExpenseFormStatus;
import com.xiaowei.expensereimbursement.status.RequestFormStatus;
import com.xiaowei.expensereimbursement.utils.ExpenseFormUtils;
import com.xiaowei.mq.sender.MessagePushSender;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;


@Service
public class ExpenseFormServiceImpl extends BaseServiceImpl<ExpenseForm> implements IExpenseFormService {

    @Autowired
    private ExpenseFormRepository expenseFormRepository;
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    @Autowired
    private ExpenseFormItemRepository expenseFormItemRepository;
    @Autowired
    private WorkOrderSelectRepository workOrderSelectRepository;
    @Autowired
    private RequestFormRepository requestFormRepository;
    @Autowired
    private MessagePushSender messagePushSender;

    public ExpenseFormServiceImpl(@Qualifier("expenseFormRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ExpenseForm saveExpenseForm(ExpenseForm expenseForm) {
        //判定参数是否合规
        judgeAttribute(expenseForm, JudgeType.INSERT);
        expenseFormRepository.save(expenseForm);

        //判断所有明细的费用科目是否唯一以及合计金额是否正确
        //保存科目明细
        judgeItemIsUniqueAndAmount(expenseForm);
        //修改工单状态为报销中
        messagePushSender.sendWorkOrderExpenseingMessage(expenseForm.getWorkOrderCode());
        return expenseForm;
    }

    private void judgeAttribute(ExpenseForm expenseForm, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            expenseForm.setId(null);
            expenseForm.setTurnDownCount(0);//初始化驳回次数
            expenseForm.setCode(getCurrentDayMaxCode());
            expenseForm.setCreatedTime(new Date());
            //验证所属工单
            judgeWorkOrder(expenseForm);
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String expenseFormId = expenseForm.getId();
            EmptyUtils.assertString(expenseFormId, "没有传入对象id");
            Optional<ExpenseForm> optional = expenseFormRepository.findById(expenseFormId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
            final ExpenseForm one = optional.get();
            if (!ArrayUtils.contains(ExpenseFormUtils.CANUPDATE, one.getStatus())) {
                throw new BusinessException("报销单当前不允许修改");
            }

            //设置无法修改的属性
            expenseForm.setTurnDownCount(one.getTurnDownCount());//驳回次数无法修改
            expenseForm.setWorkOrderCode(one.getWorkOrderCode());
        }

    }

    private void judgeWorkOrder(ExpenseForm expenseForm) {
        final String workOrderCode = expenseForm.getWorkOrderCode();
        EmptyUtils.assertString(workOrderCode, "没有传入所属工单编号");
        final WorkOrderSelect workOrderSelect = workOrderSelectRepository.findByCode(workOrderCode);
        EmptyUtils.assertObject(workOrderSelect, "没有查询到所属工单");
        //如果工单已归档,则抛出异常
        if (workOrderSelect.getSystemStatus() != 7) {
            throw new BusinessException("该工单状态无法创建报销单!");
        }
    }

    /**
     * 判断所有明细的费用科目是否唯一以及合计金额是否正确
     *
     * @param expenseForm
     */
    private void judgeItemIsUniqueAndAmount(ExpenseForm expenseForm) {
        double total = 0;
        Set<String> subjectCodes = new HashSet<>();
        final List<ExpenseFormItem> expenseFormItems = expenseForm.getExpenseFormItems();
        for (ExpenseFormItem expenseFormItem : expenseFormItems) {
            total = total + expenseFormItem.getFillFigure();
            if (subjectCodes.contains(expenseFormItem.getSubjectCode())) {
                throw new BusinessException("报销费用科目重复!");
            } else {
                subjectCodes.add(expenseFormItem.getSubjectCode());
            }
            expenseFormItem.setCreatedTime(new Date());
            if (ExpenseFormStatus.DRAFT.getStatus().equals(expenseForm.getStatus())) {
                //如果是草稿,则明细也存草稿
                expenseFormItem.setStatus(ExpenseFormItemStatus.DRAFT.getStatus());
            } else {
                expenseFormItem.setStatus(ExpenseFormItemStatus.NORMAL.getStatus());
            }
            expenseFormItem.setExpenseForm(expenseForm);
            expenseFormItemRepository.save(expenseFormItem);
        }
        //判断金额
        if (total != expenseForm.getFillAmount()) {
            throw new BusinessException("填报总金额有误!");
        }

    }

    private String getCurrentDayMaxCode() {
        String code = "FYBX" + DateUtils.getCurrentDate();
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
    public ExpenseForm updateExpenseForm(ExpenseForm expenseForm) {
        //判定参数是否合规
        judgeAttribute(expenseForm, JudgeType.UPDATE);
        expenseFormRepository.save(expenseForm);
        //判断所有明细的费用科目是否唯一以及合计金额是否正确
        expenseFormItemRepository.deleteByExpenseFormId(expenseForm.getId());
        //先删除明细,再保存明细
        judgeItemIsUniqueAndAmount(expenseForm);
        return expenseForm;
    }

    /**
     * 报销单初审
     *
     * @param expenseForm
     * @return
     */
    @Override
    @Transactional
    public ExpenseForm firstAudit(ExpenseForm expenseForm, Boolean audit) {
        final String expenseFormId = expenseForm.getId();
        EmptyUtils.assertString(expenseFormId, "没有传入对象id");
        Optional<ExpenseForm> optional = expenseFormRepository.findById(expenseFormId);
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        ExpenseForm one = optional.get();
        //判断状态
        if (!ExpenseFormStatus.PREAUDIT.getStatus().equals(one.getStatus()) &&
                !ExpenseFormStatus.TURNDOWN.getStatus().equals(one.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        //判断初审总计金额是否合规
        judgeFirstTrialAmount(expenseForm, one);
        //判断初审报销单明细
        double totalAmount = judgeFirstItem(expenseForm, one);
        if (totalAmount != expenseForm.getFirstTrialAmount()) {
            //如果明细累加的初审总计金额与前端传送过来的总计金额不一致,则抛出异常
            throw new BusinessException("初审总计金额有误!");
        }

        //判断最终初审人是否在初审人里面
        judgeFirstAudit(expenseForm, one);
        one.setFirstAuditTime(new Date());//初审时间
        if (audit) {//是否驳回
            one.setStatus(ExpenseFormStatus.FIRSTAUDIT.getStatus());
        } else {
            //设置驳回次数
            one.setTurnDownCount(one.getTurnDownCount() == null ? 1 : (one.getTurnDownCount() + 1));
            one.setStatus(ExpenseFormStatus.TURNDOWN.getStatus());
        }
        one.setFirstOption(expenseForm.getFirstOption());//初审意见
        return expenseFormRepository.save(one);
    }

    /**
     * 报销单复审
     *
     * @param expenseForm
     * @return
     */
    @Override
    @Transactional
    public ExpenseForm secondAudit(ExpenseForm expenseForm, Boolean audit) {
        final String expenseFormId = expenseForm.getId();
        EmptyUtils.assertString(expenseFormId, "没有传入对象id");
        Optional<ExpenseForm> optional = expenseFormRepository.findById(expenseFormId);
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        ExpenseForm one = optional.get();
        //判断状态
        if (!ExpenseFormStatus.FIRSTAUDIT.getStatus().equals(one.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        //判断复审总计金额是否合规
        judgeSecondTrialAmount(expenseForm, one);
        //判断复审报销单明细
        double totalAmount = judgeSecondItem(expenseForm, one);
        if (totalAmount != expenseForm.getSecondTrialAmount()) {
            //如果明细累加的复审总计金额与前端传送过来的总计金额不一致,则抛出异常
            throw new BusinessException("复审总计金额有误!");
        }

        //判断最终复审人是否在复审人里面
        judgeSecondAudit(expenseForm, one);
        one.setSecondAuditTime(new Date());//复审时间
        if (audit) {//是否驳回
            one.setStatus(ExpenseFormStatus.SECONDAUDIT.getStatus());
        } else {
            one.setStatus(ExpenseFormStatus.TURNDOWN.getStatus());
        }
        one.setSecondOption(expenseForm.getSecondOption());//复审意见
        return expenseFormRepository.save(one);
    }

    @Override
    public Map<String, Object> auditCountByUserId(String userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("preRequestCount", requestFormRepository.findPreRequestCountCount(userId,RequestFormStatus.PREAUDIT.getStatus()));
        dataMap.put("preFirstCount", expenseFormRepository.findFirstTrialCount(userId, ExpenseFormStatus.PREAUDIT.getStatus()));
        dataMap.put("preSecondCount", expenseFormRepository.findSecondTrialCount(userId, ExpenseFormStatus.FIRSTAUDIT.getStatus()));
        dataMap.put("requestCount", requestFormRepository.findRequestCountCount(userId));
        dataMap.put("firstAuditCount", expenseFormRepository.findFirstAuditCount(userId));
        dataMap.put("secondAuditCount", expenseFormRepository.findSecondAuditCount(userId));
        return dataMap;
    }

    @Override
    public Map<String, Object> expenserCount(String userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("auditCount", expenseFormRepository.findByUserIdAndStatusIn(userId,new Integer[]{ExpenseFormStatus.PREAUDIT.getStatus(),
                ExpenseFormStatus.FIRSTAUDIT.getStatus()}));
        dataMap.put("turndownCount", expenseFormRepository.findByUserIdAndStatus(userId, ExpenseFormStatus.TURNDOWN.getStatus()));
        dataMap.put("agreedCount", expenseFormRepository.findByUserIdAndStatus(userId, ExpenseFormStatus.SECONDAUDIT.getStatus()));
        dataMap.put("draftCount", expenseFormRepository.findByUserIdAndStatus(userId, ExpenseFormStatus.DRAFT.getStatus()));
        return dataMap;
    }

    private void judgeSecondTrialAmount(ExpenseForm expenseForm, ExpenseForm one) {
        final Double secondTrialAmount = expenseForm.getSecondTrialAmount();
        EmptyUtils.assertObject(secondTrialAmount, "没有传入复审总计金额");
        if (secondTrialAmount > one.getFirstTrialAmount()) {
            //不允许复审总计金额大于初审总计金额
            throw new BusinessException("不允许复审总计金额大于初审总计金额");
        } else {
            one.setSecondTrialAmount(secondTrialAmount);//设置初审总金额
        }
    }

    private void judgeSecondAudit(ExpenseForm expenseForm, ExpenseForm one) {
        EmptyUtils.assertObject(expenseForm.getSecondAudit(), "没有传入最终复审人");
        Long count = expenseFormRepository.findCountSecondTrial(expenseForm.getId(), expenseForm.getSecondAudit().getId());
        if (count == 0) {
            throw new BusinessException("该报销单没有该复审人");
        }
        one.setSecondAudit(expenseForm.getSecondAudit());//设置最终复审人
    }

    private void judgeFirstAudit(ExpenseForm expenseForm, ExpenseForm one) {
        EmptyUtils.assertObject(expenseForm.getFirstAudit(), "没有传入最终初审人");
        Long count = expenseFormRepository.findCountFirstTrial(expenseForm.getId(), expenseForm.getFirstAudit().getId());
        if (count == 0) {
            throw new BusinessException("该报销单没有该初审人");
        }
        one.setFirstAudit(expenseForm.getFirstAudit());//设置最终初审人
    }

    /**
     * 判断复审报销单明细
     *
     * @param expenseForm
     * @param one
     */
    private double judgeSecondItem(ExpenseForm expenseForm, ExpenseForm one) {
        double totalAmount = 0;
        List<ExpenseFormItem> expenseFormItems = expenseForm.getExpenseFormItems();
        List<ExpenseFormItem> oneExpenseFormItems = one.getExpenseFormItems();
        for (ExpenseFormItem oneItem : oneExpenseFormItems) {
            Optional<ExpenseFormItem> optional = expenseFormItems.stream().filter(expenseFormItem -> expenseFormItem.getId().equals(oneItem.getId())).findAny();
            if (!optional.isPresent()) {
                throw new BusinessException("费用明细有误");
            }
            final Double secondFigure = optional.get().getSecondFigure();
            EmptyUtils.assertObject(secondFigure, "没有明细初审金额");
            if (secondFigure > oneItem.getFirstFigure()) {
                throw new BusinessException("费用明细复审金额大于初审金额");
            }
            oneItem.setSecondFigure(secondFigure);
            Integer status = optional.get().getStatus();
            EmptyUtils.assertObject(status, "没有传入明细状态");
            oneItem.setStatus(status);
            if (!ExpenseFormItemStatus.REFUSE.getStatus().equals(status)) {//非不予报销
                totalAmount = totalAmount + secondFigure;
            }
            expenseFormItemRepository.save(oneItem);
        }
        return totalAmount;
    }

    /**
     * 判断初审报销单明细
     *
     * @param expenseForm
     * @param one
     */
    private double judgeFirstItem(ExpenseForm expenseForm, ExpenseForm one) {
        double totalAmount = 0;
        List<ExpenseFormItem> expenseFormItems = expenseForm.getExpenseFormItems();
        List<ExpenseFormItem> oneExpenseFormItems = one.getExpenseFormItems();
        for (ExpenseFormItem oneItem : oneExpenseFormItems) {
            Optional<ExpenseFormItem> optional = expenseFormItems.stream().filter(expenseFormItem -> expenseFormItem.getId().equals(oneItem.getId())).findAny();
            if (!optional.isPresent()) {
                throw new BusinessException("费用明细有误");
            }
            final Double firstFigure = optional.get().getFirstFigure();
            EmptyUtils.assertObject(firstFigure, "没有明细初审金额");
            if (firstFigure > oneItem.getFillFigure()) {
                throw new BusinessException("费用明细初审金额大于填报金额");
            }
            oneItem.setFirstFigure(firstFigure);
            Integer status = optional.get().getStatus();
            EmptyUtils.assertObject(status, "没有传入明细状态");
            oneItem.setStatus(status);
            if (!ExpenseFormItemStatus.REFUSE.getStatus().equals(status)) {//非不予报销
                totalAmount = totalAmount + firstFigure;
            }
            expenseFormItemRepository.save(oneItem);
        }
        return totalAmount;
    }


    /**
     * 验证初审总计金额是否合规
     *
     * @param expenseForm
     * @param one
     */
    private void judgeFirstTrialAmount(ExpenseForm expenseForm, ExpenseForm one) {
        final Double firstTrialAmount = expenseForm.getFirstTrialAmount();
        EmptyUtils.assertObject(firstTrialAmount, "没有传入初审总计金额");
        if (firstTrialAmount > one.getFillAmount()) {
            //不允许初审总计金额大于填报总计金额
            throw new BusinessException("不允许初审总计金额大于填报总计金额");
        } else {
            one.setFirstTrialAmount(firstTrialAmount);//设置初审总金额
        }
    }
}
