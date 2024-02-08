package com.kuui.kas.application.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditing {

    @CreatedBy
    String CreatedName;

    @LastModifiedBy
    String UpdatedName;

    @CreatedDate
    LocalDateTime CreatedDate;

    @LastModifiedDate
    LocalDateTime UpdatedDate;
}
