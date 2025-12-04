package org.nextme.point_service.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.nextme.point_service.domain.Point;

import lombok.Builder;

// 포인트 적립 내역 응답 DTO
@Builder
public record PointHistoryResponse(
	UUID pointId,
	UUID promotionId,
	String promotionName,
	Integer amount,
	Long queuePosition,
	LocalDateTime earnedAt
) {
	public static PointHistoryResponse from(Point point) {
		return PointHistoryResponse.builder()
			.pointId(point.getId())
			.promotionId(point.getPromotionId())
			.promotionName(point.getPromotionName())
			.amount(point.getAmount())
			.queuePosition(point.getQueuePosition())
			.earnedAt(point.getEarnedAt())
			.build();
	}
}
