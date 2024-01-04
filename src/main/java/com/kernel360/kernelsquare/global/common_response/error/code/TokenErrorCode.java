package com.kernel360.kernelsquare.global.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.TokenServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, TokenServiceStatus.INVALID_TOKEN, "유효하지 않은 토큰 입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, TokenServiceStatus.EXPIRED_TOKEN, "만료된 토큰 입니다."),
	UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, TokenServiceStatus.UNAUTHORIZED_TOKEN, "지원하지 않는 토큰 입니다."),
	WRONG_TOKEN(HttpStatus.UNAUTHORIZED, TokenServiceStatus.WRONG_TOKEN, "잘못된 토큰 입니다."),
	EXPIRED_LOGIN_INFO(HttpStatus.UNAUTHORIZED, TokenServiceStatus.EXPIRED_LOGIN_INFO, "로그인 정보가 만료 되었습니다.");

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public Integer getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMsg() {
		return msg;
	}
}