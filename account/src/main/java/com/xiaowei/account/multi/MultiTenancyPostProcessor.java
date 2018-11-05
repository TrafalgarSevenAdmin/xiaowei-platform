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

    public static enum SecurecyAdvice implements MethodInterceptor {
        INSTANCE;

        private EntityManager entityManager;

        public static final String notFilterTenancy = "!#NOT_FILTER_TENANCY_ID#!";

        /**
         * 临时指定要查询的租户
         * 若为空，就从登陆用户中获取
         * 可设置为@See com.xiaowei.account.multi.MultiTenancyPostProcessor.SecurecyAdvice#notFilterTenancy，就不筛选租户
         */
        public static ThreadLocal<String> tempChageTenancyId = new ThreadLocal<>();

        public EntityManager getEntityManager() {
            if (this.entityManager == null) {
                this.entityManager = ContextUtils.getApplicationContext().getBean(EntityManager.class);
            }
            return entityManager;
        }

        public static String getTenancyId() {
            //如果遇到临时的
            if (tempChageTenancyId.get()!=null) {
                return tempChageTenancyId.get();
            }
            LoginUserBean loginUserOrNull = LoginUserUtils.getLoginUserOrNull();
            if (loginUserOrNull != null && StringUtils.isNotEmpty(loginUserOrNull.getTenancyId())) {
                return loginUserOrNull.getTenancyId();
            } else {
                return null;
            }
        }

        @Override
        public Object invoke(MethodInvocation Invocation) throws Throwable {
            LoginUserBean loginUserOrNull = LoginUserUtils.getLoginUserOrNull();
            String tenancyId = getTenancyId();
            if (!Invocation.getMethod().getName().equals("getOne") && tenancyId !=null && !notFilterTenancy.equals(tenancyId)) {
                //补全多租户id
                try {
                    getEntityManager().unwrap(Session.class).enableFilter(MultiTenancyFilterName).setParameter("tenancyId", tenancyId);
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