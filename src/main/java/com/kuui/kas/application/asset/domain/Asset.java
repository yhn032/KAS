package com.kuui.kas.application.asset.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.file.domain.SaveFile;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

//기본키, 재고 번호, 재고 이름, 재고 수량(=현재 수량), 등록자, 수정자, 등록일자, 수정일자, 재고 위치 번호

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

    @Column(name = "asset_total_cnt")
    private Long assetTotalCnt;

    @Column(name = "asset_remain_cnt")
    private Long assetRemainCnt;

    @Column(name = "reg_teacher_name")
    private String regTeacherName;

    @Column(name = "asset_ctg")
    private String assetCtg;


    @Column(name = "upd_teacher_name")
    private String updTeacherName;

    @Column(name = "asset_reg_date")
    String assetRegDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Column(name = "asset_upd_date")
    String assetUpdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Column(name = "asset_position")
    Integer assetPos;

    @JsonBackReference
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaveFile> assetImgs;

    @OneToMany(mappedBy = "boardAsset")
    private List<Board> boards;

    public Asset(String assetId, String assetNo, String assetName, Long assetTotalCnt, Long assetRemainCnt, String assetCtg, int assetPos,String regTeacherName, String updTeacherName) {
        this.assetId = assetId;
        this.assetNo = assetNo;
        this.assetName = assetName;
        this.assetTotalCnt = assetTotalCnt;
        this.assetRemainCnt = assetRemainCnt;
        this.assetCtg = assetCtg;
        this.assetPos = assetPos;
        this.regTeacherName = regTeacherName;
        this.updTeacherName = updTeacherName;
    }
}
