package org.nextme.point_service.global.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

	// Promotion Service로부터 받는 토픽
	public static final String PROMOTION_WINNER_TOPIC = "promotion.winner";

	// User Service로 발행하는 토픽
	public static final String USER_POINT_EARNED_TOPIC = "user.point.earned";
}
