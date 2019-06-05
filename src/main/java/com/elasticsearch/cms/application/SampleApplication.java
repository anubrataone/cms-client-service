package com.elasticsearch.cms.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan("com.elasticsearch.cms")
public class SampleApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

}

