package com.xiaowei;

import com.xiaowei.commonupload.BaseUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WorkSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkSystemApplication.class, args);
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
