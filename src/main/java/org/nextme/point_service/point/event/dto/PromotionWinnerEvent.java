package org.nextme.point_service.point.event.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
프로모션 당첨 이벤트 DTO
RabbitMQ를 통해 Promotion Service로부터 수신
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionWinnerEvent {

	// 프로모션 ID
	private UUID promotionId;

	// 프로모션 이름
	private String promotionName;

	// 당첨자 사용자 ID
	private UUID userId;

	// 지급할 포인트 금액
	private Integer pointAmount;

	// 당첨 순번
	private Long queuePosition;

	// 당첨 시각
	private LocalDateTime wonAt;
}
