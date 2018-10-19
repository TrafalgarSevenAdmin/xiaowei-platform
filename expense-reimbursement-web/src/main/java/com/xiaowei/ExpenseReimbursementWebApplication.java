package com.xiaowei;

import com.xiaowei.account.multi.MultiTenancyRepositoryFactoryBean;
import com.xiaowei.commonupload.BaseUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = MultiTenancyRepositoryFactoryBean.class)
public class ExpenseReimbursementWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseReimbursementWebApplication.class, args);
    }

    /**
     * 配置文件上传通用服务
     * @return
     */
    @Bean
    public BaseUploadController baseUploadController(){
        return new BaseUploadController();
    }

}
