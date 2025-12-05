package org.nextme.point_service.global.exception;

import org.nextme.infrastructure.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointErrorCode {

	// 포인트 관련 에러
	POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "POINT_4001", "포인트 내역을 찾을 수 없습니다."),
	POINT_ALREADY_GRANTED(HttpStatus.CONFLICT, "POINT_4002", "이미 포인트가 지급되었습니다."),

	// 사용자 관련 에러
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "POINT_4003", "사용자를 찾을 수 없습니다."),

	// 내부 서버 에러
	POINT_GRANT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "POINT_5001", "포인트 지급에 실패했습니다."),
	EVENT_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "POINT_5002", "이벤트 처리에 실패했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String defaultMessage;

	public ApplicationException toException() {
		return new ApplicationException(this.httpStatus, this.code, this.defaultMessage);
	}

	public ApplicationException toException(String customMessage) {
		return new ApplicationException(this.httpStatus, this.code, customMessage);
	}
}
