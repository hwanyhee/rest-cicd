package com.ictedu.springrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(
					//스웨거 웹 UI에서 API 정보인 제목,설명,버전 정보를 설정
					new Info()
					.title("Spring Boot REST API Specifications")
					.description("<h2>This document is REST API Specification</h2>")
					.version("1.0.0")
					
				);
	}
}
