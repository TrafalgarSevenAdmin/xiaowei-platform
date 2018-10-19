package com.xiaowei.account.multi;

import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.context.ContextUtils;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import javax.persistence.EntityManager;

import static com.xiaowei.account.multi.consts.MultiConsts.MultiTenancyFilterName;

@Log
public class MultiTenancyPostProcessor implements RepositoryProxyPostProcessor {

    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(SecurecyAdvice.INSTANCE);
    }

    static enum SecurecyAdvice implements MethodInterceptor {
        INSTANCE;

        private EntityManager entityManager;

        public EntityManager getEntityManager() {
            if (this.entityManager == null) {
                this.entityManager = ContextUtils.getApplicationContext().getBean(EntityManager.class);
            }
            return entityManager;
        }

        @Override
        public Object invoke(MethodInvocation Invocation) throws Throwable {
            LoginUserBean loginUserOrNull = LoginUserUtils.getLoginUserOrNull();
            if (!Invocation.getMethod().getName().equals("getOne") && loginUserOrNull != null && StringUtils.isNotEmpty(loginUserOrNull.getTenancyId())) {
                //补全多租户id
                try {
                    getEntityManager().unwrap(Session.class).enableFilter(MultiTenancyFilterName).setParameter("tenancyId", loginUserOrNull.getTenancyId());
                    Object obj = Invocation.proceed();
                    return obj;
                } finally {
                    getEntityManager().unwrap(Session.class).disableFilter(MultiTenancyFilterName);
                }
            }
            return Invocation.proceed();
        }
    }
}