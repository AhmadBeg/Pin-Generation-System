package com.sample.excercise.pingeneration;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@ComponentScan(basePackages = "com.sample.excercise.pingeneration.*")
@PropertySource("classpath:application-${app_environment:dev}.properties")
@EnableAsync
@EnableScheduling
public class PinGenerationApplication {

    public static void main(String[] args) {
	SpringApplication.run(PinGenerationApplication.class, args);
    }

    @PostConstruct
    public void init() {
	//TODO print all properties here and anything else we want to print here?
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
	CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
	filter.setIncludeQueryString(true);
	filter.setIncludePayload(true);
	filter.setMaxPayloadLength(10000);
	filter.setIncludeHeaders(true);
	filter.setAfterMessagePrefix("REQUEST DATA : ");
	return filter;
    }
}
