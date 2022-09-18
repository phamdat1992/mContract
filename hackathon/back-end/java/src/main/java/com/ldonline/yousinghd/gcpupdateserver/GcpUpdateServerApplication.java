package com.ldonline.yousinghd.gcpupdateserver;

import com.ldonline.spring.rythm.SimpleRythmViewResolver;
import com.ldonline.yousinghd.gcpupdateserver.interceptor.UserActiveSecurityApiInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class GcpUpdateServerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(GcpUpdateServerApplication.class, args);

	}

	@Bean
	public ViewResolver getViewResolver() {
		SimpleRythmViewResolver resolver = new SimpleRythmViewResolver();
		resolver.setRootDirectory("rythm/views/");
		resolver.setSuffix(".html");
		return resolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userActiveApiSecurityInterceptor())//
				.addPathPatterns("/3.0/auth/dl/**");
	}

	@Bean
	public UserActiveSecurityApiInterceptor userActiveApiSecurityInterceptor() {
		return new UserActiveSecurityApiInterceptor();
	}
}
