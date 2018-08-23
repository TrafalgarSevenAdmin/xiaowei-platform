package com.xiaowei.flow.extend.simple;

import com.google.common.collect.Sets;
import com.xiaowei.flow.extend.LoginUser;
import com.xiaowei.flow.extend.TaskComplete;
import com.xiaowei.flow.extend.TaskNodeComplete;
import com.xiaowei.flow.extend.TaskNotifyExtend;
import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;
import com.xiaowei.flow.pojo.TaskNotifyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Slf4j
@Configuration
public class DefaultExtend {

    @Bean
    @ConditionalOnMissingBean
    public LoginUser loginUser() {
        return new LoginUser() {
            @Override
            public String getUserId() {
                return "1";
            }

            @Override
            public String getUserName() {
                return "管理员";
            }

            @Override
            public Set<String> getRoleIds() {
                return Sets.newHashSet();
            }

            @Override
            public Set<String> getDepartmentIds() {
                return Sets.newHashSet();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskNodeComplete taskNodeComplete() {
        return new TaskNodeComplete() {
            @Override
            public TaskCompleteExtendResult execute(TaskCompleteExtendParameter parameter) {
                return TaskCompleteExtendResult.builder().build();
            }

            @Override
            public void complete(TaskCompleteExtendParameter parameter) {

            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskComplete taskComplete() {
        return new TaskComplete() {
            @Override
            public void complete(TaskCompleteExtendParameter parameter) {
                log.info("任务{}完成！", parameter.getTask().getName());
            }
        };
    }
    @Bean
    @ConditionalOnMissingBean
    public TaskNotifyExtend taskNotifyExtend() {
        return new TaskNotifyExtend() {
            @Override
            public void notifyAction(TaskNotifyContext context) {
                log.info("任务代做/变更通知,通知对象{}", context.toString());
            }
        };
    }

}
