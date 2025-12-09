package org.nextme.point_service.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Point Service API")
				.description("포인트 관리 서비스 API 문서")
				.version("v1.0.0"))
			.servers(List.of(
				new Server()
					.url("http://localhost:8081")
			));
	}
}
