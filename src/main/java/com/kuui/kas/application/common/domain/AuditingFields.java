package com.kuui.kas.application.common.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public interface AuditingFields {
    @CreatedBy
    String getCreatedName();

    @LastModifiedBy
    String getUpdatedName();

    @CreatedDate
    LocalDateTime getCreatedDate();

    @LastModifiedDate
    LocalDateTime getUpdatedDate();
}
