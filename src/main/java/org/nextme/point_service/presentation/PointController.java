package org.nextme.point_service.presentation;

import java.util.List;
import java.util.UUID;

import org.nextme.infrastructure.success.CustomResponse;
import org.nextme.point_service.application.PointService;
import org.nextme.point_service.presentation.dto.PointHistoryResponse;
import org.nextme.point_service.presentation.dto.PointSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/points")
@RequiredArgsConstructor
@Tag(name = "Point", description = "포인트 관리 API")
public class PointController {

	private final PointService pointService;

	/*
	포인트 적립 내역 조회 API
	@param userId 사용자 ID
	@return 포인트 적립 내역 리스트
	 */
	@Operation(summary = "포인트 적립 내역 조회", description = "사용자의 포인트 적립 내역을 최신순으로 조회합니다.")
	@GetMapping("/users/{userId}/history")
	public ResponseEntity<CustomResponse<List<PointHistoryResponse>>> getPointHistory(
		@Parameter(description = "사용자 ID", required = true)
		@PathVariable UUID userId
	) {
		List<PointHistoryResponse> response = pointService.getPointHistory(userId);
		return ResponseEntity.ok(CustomResponse.onSuccess(response));
	}

	/*
	포인트 요약 조회 API
	@param userId 사용자 ID
	@return 총 포인트 및 적립 횟수
	 */
	@Operation(summary = "포인트 요약 조회", description = "사용자의 총 포인트와 적립 횟수를 조회합니다.")
	@GetMapping("/users/{userId}/summary")
	public ResponseEntity<CustomResponse<PointSummaryResponse>> getPointSummary(
		@Parameter(description = "사용자 ID", required = true)
		@PathVariable UUID userId
	) {
		PointSummaryResponse response = pointService.getPointSummary(userId);
		return ResponseEntity.ok(CustomResponse.onSuccess(response));
	}
}
