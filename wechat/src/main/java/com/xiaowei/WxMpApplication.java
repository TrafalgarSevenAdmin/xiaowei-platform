package com.xiaowei;

import com.xiaowei.account.multi.MultiTenancyRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = MultiTenancyRepositoryFactoryBean.class)
public class WxMpApplication {

  public static void main(String[] args) {
    SpringApplication.run(WxMpApplication.class, args);
  }
}
