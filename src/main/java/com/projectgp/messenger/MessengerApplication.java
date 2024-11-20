package com.projectgp.messenger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
//启用定时任务支持
@EnableScheduling
@MapperScan("com.projectgp.messenger.mapper")
public class MessengerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessengerApplication.class, args);
	}

}
