package com.youblog.authorization;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ConfigureCustomInterceptor implements WebMvcConfigurer {

	private final CustomInterceptor customInterceptor = new CustomInterceptor();

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customInterceptor);
	}

}
