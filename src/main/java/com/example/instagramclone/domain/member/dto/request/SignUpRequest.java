package com.example.instagramclone.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SignUpRequest(

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Pattern(regexp = "^[a-z0-9._]{4,20}$", message = "사용자 이름은 4~20자의 영문 소문자, 숫자, 마침표(.), 밑줄(_)만 사용 가능합니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        // 💡 복잡한 비밀번호 규칙을 정규식(RegExp)으로 한 번에 검증합니다.
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @NotBlank(message = "이메일 또는 전화번호는 필수입니다.")
        String emailOrPhone, // 👈 핵심: 입력값을 하나로 받아서 서버가 판단합니다.

        @NotBlank(message = "이름은 필수입니다.")
        String name
) {
        // 유틸메서드, 정적메서드...
}
