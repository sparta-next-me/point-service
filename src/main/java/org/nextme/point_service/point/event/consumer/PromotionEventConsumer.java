package org.nextme.point_service.point.event.consumer;

import java.util.Optional;

import org.nextme.point_service.global.config.RabbitMQConfig;
import org.nextme.point_service.point.domain.Point;
import org.nextme.point_service.point.domain.PointRepository;
import org.nextme.point_service.point.event.dto.PromotionWinnerEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 프로모션 당첨 이벤트 컨슈머
@Slf4j
@Component
@RequiredArgsConstructor
public class PromotionEventConsumer {

	private final PointRepository pointRepository;

	// 당첨 이벤트 처리
	@RabbitListener(queues = RabbitMQConfig.WINNER_QUEUE)
	public void handleWinnerEvent(PromotionWinnerEvent event) {
		log.info("당첨 이벤트 수신 : promotionId={}, userId={}, amount={}",
			event.getPromotionId(), event.getUserId(), event.getPointAmount());
		try {
			// 1. 중복 지급 방지 체크
			Optional<Point> existing = pointRepository.findByPromotionIdAndUserId(
				event.getPromotionId(), event.getUserId());

			if (existing.isPresent()) {
				log.warn("이미 포인트가 지급된 사용자입니다 : userId={}, promotionId={}",
					event.getUserId(), event.getPromotionId());
				return;
			}

			// 2. 포인트 생성 및 지급
			Point point = Point.create(
				event.getPromotionId(),
				event.getPromotionName(),
				event.getUserId(),
				event.getPointAmount(),
				event.getQueuePosition(),
				event.getWonAt()
			);

			// 3. DB 저장
			pointRepository.save(point);

			log.info("포인트 지급 완료 : userId={}, amount={}, promotionName={}",
				event.getUserId(), event.getPointAmount(), event.getPromotionName());
		} catch (Exception e) {
			log.error("포인트 지급 실패 : userId={}, error={}",
				event.getUserId(), e.getMessage(), e);
			throw e;
		}
	}
}
