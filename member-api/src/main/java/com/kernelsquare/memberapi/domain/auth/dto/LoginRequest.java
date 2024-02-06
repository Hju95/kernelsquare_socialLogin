package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
	@NotBlank(message = "이메일을 입력해 주세요.", groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 5, max = 40, message = "이메일 길이를 확인해 주세요.", groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = "이메일에 한글은 허용되지 않습니다.", groups = ValidationGroups.PatternGroup.class)
	@Email(message = "올바른 이메일을 입력해 주세요.", groups = ValidationGroups.EmailGroup.class)
	String email,

	@NotBlank(message = "비밀번호를 입력해 주세요.", groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 8, max = 16, message = "비밀번호 길이를 확인해 주세요.", groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
		message = "영문 대소문자, 특수문자, 숫자를 각각 1자 이상 포함하세요.", groups = ValidationGroups.PatternGroup.class)
	String password
) {
}
