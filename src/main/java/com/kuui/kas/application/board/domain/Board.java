package com.kuui.kas.application.board.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    Long boardId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset boardAsset;

    @ManyToOne
    private Teacher boardTeacher;

    @Column(name = "board_carry_in_name")
    String boardCarryInName;

    @Column(name = "board_asset_return_yn")
    String boardAssetReturnYn;

    @Column(name = "board_share_count")
    Long boardShareCount;

    @Column(name = "board_reg_date")
    String boardRegDate;
}
