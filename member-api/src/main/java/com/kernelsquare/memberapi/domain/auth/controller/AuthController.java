package com.kernelsquare.memberapi.domain.auth.controller;


import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.validation.ValidationSequence;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.auth.dto.*;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
		final @RequestBody @Validated(ValidationSequence.class) LoginRequest loginRequest) {
		Member member = authService.login(loginRequest);
		TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);
		LoginResponse loginResponse = LoginResponse.of(member, tokenResponse);

		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, loginResponse);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
		final @RequestBody @Validated(ValidationSequence.class) SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);
		return ResponseEntityFactory.toResponseEntity(SIGN_UP_SUCCESS, signUpResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissueAccessToken(
		final @RequestBody TokenRequest requestTokenRequest) {
		TokenResponse tokenResponse = tokenProvider.reissueToken(requestTokenRequest);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenResponse);
	}

	@PostMapping("/auth/check/email")
	public ResponseEntity<ApiResponse> isEmailUnique(
		final @RequestBody @Validated(ValidationSequence.class) CheckDuplicateEmailRequest emailRequest) {
		authService.isEmailUnique(emailRequest.email());
		return ResponseEntityFactory.toResponseEntity(EMAIL_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/check/nickname")
	public ResponseEntity<ApiResponse> isNicknameUnique(
		final @RequestBody @Validated(ValidationSequence.class) CheckDuplicateNicknameRequest nicknameRequest) {
		authService.isNicknameUnique(nicknameRequest.nickname());
		return ResponseEntityFactory.toResponseEntity(NICKNAME_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenRequest tokenRequest) {
		tokenProvider.logout(tokenRequest);
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}

	@GetMapping("/test")
	public ResponseEntity<String> testEndpoint(HttpServletRequest request) {
		// 모든 쿠키 가져오기
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// 'loginResponse'라는 이름의 쿠키 찾기
				if ("loginResponse".equals(cookie.getName())) {
					// 쿠키의 값을 응답으로 반환
					String responseBody = "{\"loginResponse\": \"" + cookie.getValue() + "\"}";
					return ResponseEntity.ok(responseBody);
				}
			}
		}

		// 'loginResponse'라는 이름의 쿠키가 없는 경우
		String responseBody = "{\"message\": \"No 'loginResponse' cookie.\"}";
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
	}
}