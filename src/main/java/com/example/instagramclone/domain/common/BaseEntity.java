package com.example.instagramclone.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 1. 상속받는 자식 엔티티들에게 매핑 정보를 물려줍니다.
@EntityListeners(AuditingEntityListener.class) // 2. 생성/수정 시간을 자동으로 감시해서 기록합니다.
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성일

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일
}