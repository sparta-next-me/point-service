package org.nextme.point_service.global.exception;

import org.nextme.infrastructure.exception.ApplicationException;
import org.nextme.infrastructure.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * ApplicationException 처리
	 * 비즈니스 로직에서 발생하는 커스텀 예외
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
		log.error("ApplicationException: code={}, message={}", e.getCode(), e.getMessage());
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(ErrorResponse.of(e.getHttpStatus(), e.getCode(), e.getMessage()));
	}

	/**
	 * Validation 예외 처리
	 * @Valid, @Validated 검증 실패 시 발생
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
		log.error("Validation failed: {}", e.getMessage());
		String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", errorMessage));
	}

	/**
	 * HTTP 메시지 읽기 실패 예외 처리
	 * 잘못된 JSON 형식이나 필수 RequestBody 누락 시 발생
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("HttpMessageNotReadableException: {}", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요."));
	}

	/**
	 * 타입 불일치 예외 처리
	 * PathVariable이나 RequestParam의 타입이 맞지 않을 때 발생
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error("Type mismatch: parameter={}, value={}, requiredType={}",
			e.getName(), e.getValue(), e.getRequiredType());
		String message = String.format("'%s' 파라미터의 값이 올바르지 않습니다.", e.getName());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", message));
	}

	/**
	 * 기타 예상치 못한 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Unexpected error occurred", e);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
	}
}
