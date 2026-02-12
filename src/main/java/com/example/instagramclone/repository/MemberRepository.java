package com.example.instagramclone.repository;


import com.example.instagramclone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 1. 로그인 시 아이디, 이메일, 전화번호로 회원을 찾습니다.
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone);

    // 2. 가입 전 중복 체크 3종 세트 (DB 조회를 한 번에!)
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
}


