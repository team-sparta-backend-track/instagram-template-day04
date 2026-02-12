package com.example.instagramclone.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public record SignUpResponse(
        String username,
        String message
) {
    // 정적 팩토리 메서드로 객체 생성의 가독성을 높입니다.

    public static SignUpResponse of(String username, String message) {
        return SignUpResponse.builder()
                .username(username)
                .message(message)
                .build();
    }
}
