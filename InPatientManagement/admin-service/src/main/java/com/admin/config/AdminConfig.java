package com.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AdminConfig {

    @Bean
    WebMvcConfigurer mvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				 registry.addMapping("/**")
	                .allowedOrigins("http://localhost:4200") // Add your frontend origin here
	                .allowedMethods("GET", "POST", "PUT", "DELETE")
	                .allowedHeaders("*")
	                .allowCredentials(true);
			}
		};
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
