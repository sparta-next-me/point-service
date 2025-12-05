package org.nextme.point_service.point.event.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// User Service로 발행하는 포인트 획득 이벤트
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointEarnedEvent {

	private UUID userId;			// 사용자 ID
	private Integer amount;			// 획득 포인트
	private UUID promotionId;		// 프로모션 ID
	private String promotionName;	// 프로모션 이름
	private LocalDateTime earnedAt;	// 획득 시간

	public static PointEarnedEvent of(
		UUID userId,
		Integer amount,
		UUID promotionId,
		String promotionName,
		LocalDateTime earnedAt
	) {
		return new PointEarnedEvent(
			userId,
			amount,
			promotionId,
			promotionName,
			earnedAt
		);
	}
}
