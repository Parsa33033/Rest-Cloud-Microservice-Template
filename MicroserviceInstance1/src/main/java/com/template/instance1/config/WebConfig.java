package com.template.instance1.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableEurekaClient
public class WebConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate rest = new RestTemplate();
		return rest;
	}
}
