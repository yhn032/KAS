package com.kuui.kas.application.board.domain;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long boardId;
    private Asset boardAsset;
    private Teacher boardTeacher;
    private String boardCarryInName;
    private String boardAssetReturnYn;
    private Long boardShareCount;
    private String boardRegDate;
}
