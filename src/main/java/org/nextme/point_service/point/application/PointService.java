package org.nextme.point_service.point.application;

import java.util.List;
import java.util.UUID;

import org.nextme.point_service.point.domain.Point;
import org.nextme.point_service.point.domain.PointRepository;
import org.nextme.point_service.point.presentation.dto.PointHistoryResponse;
import org.nextme.point_service.point.presentation.dto.PointSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;

	/*
	사용자의 포인트 적립 내역 조회
	@param userId 사용자 ID
	@return 포인트 내역 리스트
	 */
	@Transactional(readOnly = true)
	public List<PointHistoryResponse> getPointHistory(UUID userId) {
		List<Point> points = pointRepository.findByUserIdOrderByEarnedAtDesc(userId);
		return points.stream()
			.map(PointHistoryResponse::from)
			.toList();
	}

	/*
	사용자의 포인트 요약 정보 조회
	@param userId 사용자 ID
	@return 총 포인트, 적립 횟수
	 */
	@Transactional(readOnly = true)
	public PointSummaryResponse getPointSummary(UUID userId) {
		List<Point> points = pointRepository.findByUserIdOrderByEarnedAtDesc(userId);

		// 총 포인트 계산
		int totalPoints = points.stream()
			.mapToInt(Point::getAmount)
			.sum();

		return PointSummaryResponse.of(userId, totalPoints, points.size());
	}
}
