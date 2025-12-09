package org.nextme.point_service.point.event.consumer;

import java.util.Optional;

import org.nextme.point_service.global.config.RabbitMQConfig;
import org.nextme.point_service.global.exception.PointErrorCode;
import org.nextme.point_service.point.domain.Point;
import org.nextme.point_service.point.domain.PointRepository;
import org.nextme.point_service.point.event.dto.PointEarnedEvent;
import org.nextme.point_service.point.event.dto.PromotionWinnerEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 프로모션 이벤트 당첨 컨슈머
@Slf4j
@Component
@RequiredArgsConstructor
public class PromotionEventConsumer {

	private final PointRepository pointRepository;
	private final RabbitTemplate rabbitTemplate;

	// 당첨 이벤트 처리
	@Transactional
	@RabbitListener(queues = RabbitMQConfig.WINNER_QUEUE)
	public void handleWinnerEvent(PromotionWinnerEvent event) {
		log.info("당첨 이벤트 수신: promotionId={}, userId={}, amount={}",
			event.getPromotionId(), event.getUserId(), event.getPointAmount());

		try {
			// 1. 이벤트 데이터 검증
			validateEvent(event);

			// 2. 중복 지급 방지 체크
			Optional<Point> existing = pointRepository.findByPromotionIdAndUserId(
				event.getPromotionId(), event.getUserId()
			);

			if (existing.isPresent()) {
				log.warn("이미 포인트가 지급된 사용자입니다: userId={}, promotionId={}",
					event.getUserId(), event.getPromotionId());
				throw PointErrorCode.POINT_ALREADY_GRANTED.toException();
			}

			// 3. 포인트 생성 및 지급
			Point point = Point.create(
				event.getPromotionId(),
				event.getPromotionName(),
				event.getUserId(),
				event.getPointAmount(),
				event.getQueuePosition(),
				event.getWonAt()
			);

			// 4. DB 저장
			pointRepository.save(point);

			log.info("포인트 지급 완료: userId={}, amount={}, promotionName={}",
				event.getUserId(), event.getPointAmount(), event.getPromotionName());

			// 5. User Service로 포인트 획득 이벤트 발행
			publishPointEarnedEvent(point);

		} catch (Exception e) {
			log.error("포인트 지급 실패: userId={}, promotionId={}, error={}",
				event.getUserId(), event.getPromotionId(), e.getMessage(), e);
			// 예외를 던져서 RabbitMQ가 메시지를 재처리하도록 함
			throw PointErrorCode.POINT_GRANT_FAILED.toException("포인트 지급 실패: " + e.getMessage());
		}
	}

	// User Service로 포인트 획득 이벤트 발행
	private void publishPointEarnedEvent(Point point) {
		PointEarnedEvent earnedEvent = PointEarnedEvent.of(
			point.getUserId(),
			point.getAmount(),
			point.getPromotionId(),
			point.getPromotionName(),
			point.getEarnedAt()
		);

		rabbitTemplate.convertAndSend(
			RabbitMQConfig.USER_EXCHANGE,
			RabbitMQConfig.POINT_EARNED_ROUTING_KEY,
			earnedEvent
		);

		log.info("User Service로 포인트 획득 이벤트 발행 : userId={}, amount={}", point.getUserId(), point.getAmount());
	}

	/**
	 * 이벤트 데이터 검증
	 * @param event 당첨 이벤트
	 */
	private void validateEvent(PromotionWinnerEvent event) {
		if (event == null) {
			throw PointErrorCode.EVENT_PROCESSING_FAILED.toException();
		}
		if (event.getPromotionId() == null) {
			throw PointErrorCode.EVENT_PROCESSING_FAILED.toException();
		}
		if (event.getUserId() == null) {
			throw PointErrorCode.USER_NOT_FOUND.toException();
		}
		if (event.getPointAmount() == null || event.getPointAmount() <= 0) {
			throw PointErrorCode.EVENT_PROCESSING_FAILED.toException();
		}
	}
}
