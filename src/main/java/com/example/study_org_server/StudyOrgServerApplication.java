package com.example.study_org_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StudyOrgServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyOrgServerApplication.class, args);
	}

}
