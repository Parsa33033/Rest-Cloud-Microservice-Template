package com.template.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MicroserviceZuulGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZuulGatewayApplication.class, args);
	}

}
