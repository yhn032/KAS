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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAccumulator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final AssetService assetService;
    private final BoardService boardService;
    private final TeacherService teacherService;

    /**
     * 게시판 페이지 호출
     * @param principal
     * @param model
     * @return
     */
    @GetMapping(value = "/shareList")
    public String shareList (@RequestParam Map<String, Object> paramMap, Principal principal, Model model) {
        int page = paramMap.get("page") == null ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int pageUnit = paramMap.get("pageUnit") == null ? 10 : Integer.parseInt(paramMap.get("pageUnit").toString());

        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Board> allShareList = boardService.findAllShareList(page, pageUnit);

        //페이징 처리를 위한 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", allShareList.size() > 10 ? allShareList.size() / 10 : 1);
        model.addAttribute("pageUnit", pageUnit);

        model.addAttribute("teacherList", teachers);
        model.addAttribute("boards", allShareList);
        model.addAttribute("username", principal.getName());
        return "/board/shareList";
    }

    /**
     * 게시판 반출 대장 등록
     * @param boardAssetAssetId
     * @param boardTeacherTeacherId
     * @param boardCarryInName
     * @param boardShareCount
     * @return
     */
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
