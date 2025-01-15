package com.example.cbc_vcms_internal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CbcVcmsInternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbcVcmsInternalApplication.class, args);
	}

}
