package org.nextme.point_service.presentation.dto;

import java.util.UUID;

import lombok.Builder;

// 포인트 요약 정보 응답 DTO
@Builder
public record PointSummaryResponse(
	UUID userId,		// 사용자 ID
	Integer totalPoints,// 총 포인트
	Integer earnedCount	// 적립 횟수
) {
	// 포인트 요약 정보 생성
	public static PointSummaryResponse of(UUID userId, Integer totalPoints, Integer earnedCount) {
		return PointSummaryResponse.builder()
			.userId(userId)
			.totalPoints(totalPoints)
			.earnedCount(earnedCount)
			.build();
	}
}
