package com.kuui.kas.application.asset.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

//기본키, 재고 번호, 재고 이름, 재고 수량(=현재 수량), 등록자, 수정자, 등록일자, 수정일자

@Entity
@Getter
@NoArgsConstructor
public class Asset {

    @Id
    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "asset_no")
    private String assetNo;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "asset_cnt")
    private Long assetCnt;

    @Column(name = "reg_teacher_name")
    private String regTeacherName;

    @Column(name = "upd_teacher_name")
    private String updTeacherName;

    @Column(name = "asset_reg_date")
    @CreationTimestamp
    LocalDateTime assetRegDate = LocalDateTime.now();

    @Column(name = "asset_upd_date")
    @CreationTimestamp
    LocalDateTime assetUpdDate = LocalDateTime.now();

    public Asset(String assetId, String assetNo, String assetName, Long assetCnt, String regTeacherName, String updTeacherName) {
        this.assetId = assetId;
        this.assetNo = assetNo;
        this.assetName = assetName;
        this.assetCnt = assetCnt;
        this.regTeacherName = regTeacherName;
        this.updTeacherName = updTeacherName;
    }
}
