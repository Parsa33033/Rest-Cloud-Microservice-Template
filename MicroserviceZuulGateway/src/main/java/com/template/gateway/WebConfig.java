package com.template.gateway;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaClient
@EnableZuulProxy
public class WebConfig {

}
