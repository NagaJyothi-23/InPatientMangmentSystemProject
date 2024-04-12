package com.patient.billing.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PatientBillingServiceApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(PatientBillingServiceApplication.class, args);
	}

}
