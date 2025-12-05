package org.nextme.point_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"org.nextme.point_service", "org.nextme.infrastructure"})
public class PointServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointServiceApplication.class, args);
	}

}
