package org.nextme.point_service.point.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.nextme.common.jpa.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	// 사용자 ID
	@Column(nullable = false)
	private UUID userId;

	// 프로모션 ID
	@Column(nullable = false)
	private UUID promotionId;

	// 프로모션 이름
	@Column(nullable = false)
	private String promotionName;

	// 포인트 금액
	@Column(nullable = false)
	private Integer amount;

	// 당첨 순번
	@Column(nullable = false)
	private Long queuePosition;

	// 포인트 지급 시각
	private LocalDateTime earnedAt;

	// 포인트 생성 팩토리 메서드
	public static Point create(
		UUID promotionId,
		String promotionName,
		UUID userId,
		Integer amount,
		Long queuePosition,
		LocalDateTime earnedAt
	) {
		Point point = new Point();
		point.promotionId = promotionId;
		point.promotionName = promotionName;
		point.userId = userId;
		point.amount = amount;
		point.queuePosition = queuePosition;
		point.earnedAt = earnedAt;
		return point;
	}
}