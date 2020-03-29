package com.projectfinalwebscrappingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  //เปิดใช้งาน
public class ProjectFinalWebScrappingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectFinalWebScrappingBotApplication.class, args);
	}

}
