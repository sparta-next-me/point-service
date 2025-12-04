package org.nextme.point_service.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, UUID> {
	/*
	사용자별 포인트 내역 조회 (최신순)
	@param userId 사용자 ID
	@return 포인트 내역 리스트
	 */
	List<Point> findByUserIdOrderByEarnedAtDesc(UUID userId);

	/*
	특정 프로모션의 특정 사용자 포인트 조회
	@param promotionId 프로모션 ID
	@param userId 사용자 ID
	@return 포인트 내역
	 */
	Optional<Point> findByPromotionIdAndUserId(UUID promotionId, UUID userId);
}
