package com.template.instance1.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class MainController {

	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	RestTemplate rest;

	@GetMapping("/hello")
	@HystrixCommand(fallbackMethod = "fallback")
	public String main(@RequestHeader HttpHeaders headers) {
		logger.info("----------------------->");
		HttpEntity<String> httpEntity = new HttpEntity<String>("body", headers);
		ResponseEntity<String> response = rest.exchange("http://child/hello", HttpMethod.GET, httpEntity, String.class,
				new HashMap<String, String>());
		return response.getBody();
	}

	public String fallback() {
		return "hello from parent";
	}
}
