package com.kuui.kas.application.board.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.board.exception.NoRemainAssetException;
import com.kuui.kas.application.board.service.BoardService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.atomic.LongAccumulator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final AssetService assetService;
    private final BoardService boardService;
    private final TeacherService teacherService;

    @PostMapping("/addShare")
    @ResponseBody
    public HashMap<String, String> addShareBoard(@RequestParam String boardAssetAssetId, @RequestParam Long boardTeacherTeacherId, @RequestParam String boardCarryInName, @RequestParam Long boardShareCount) {
        HashMap<String,String> resultMap = new HashMap<>();
        Asset boardAsset = assetService.findById(boardAssetAssetId);
        Teacher boardTeacher = teacherService.findById(boardTeacherTeacherId);
        Board board = Board.builder()
                .boardAsset(boardAsset)
                .boardTeacher(boardTeacher)
                .boardCarryInName(boardCarryInName)
                .boardAssetReturnYn("N")
                .boardShareCount(boardShareCount)
                .boardRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        Board saveBoard;
        try{
            saveBoard = boardService.addShareBoard(board);

            if(saveBoard.getBoardId() != null) {
                resultMap.put("status", "success");
            }else {
                resultMap.put("status", "fail");

            }
        }catch (NoRemainAssetException e) {
            resultMap.put("status", "fail");
            resultMap.put("errorMessage", e.getMessage());
        }


        return resultMap;
    }
}
