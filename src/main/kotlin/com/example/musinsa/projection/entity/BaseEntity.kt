package com.example.musinsa.projection.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(
    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "DATETIME(6) null comment '생성일자'")
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(columnDefinition = "DATETIME(6) null comment '변경일자'")
    var updatedAt: LocalDateTime? = null
)
