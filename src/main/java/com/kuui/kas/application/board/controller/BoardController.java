package com.kuui.kas.application.board.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.board.domain.BoardDTO;
import com.kuui.kas.application.board.exception.NoRemainAssetException;
import com.kuui.kas.application.board.service.BoardService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final AssetService assetService;
    private final BoardService boardService;
    private final TeacherService teacherService;

    @GetMapping(value = "/{boardId}/return")
    @ResponseBody
    public String returnAsset(@PathVariable Long boardId) {
        log.info("=============================================");
        log.info("자산 반납");
        log.info("=============================================");
        Long bId = boardService.returnAsset(boardId);

        if(bId != 0) return "success";
        else return "fail";
    }

    //DTO를 만들어서 DTO를 반환하도록 하자 (순환참조 해결)
    @GetMapping(value = "/{boardId}/show")
    @ResponseBody
    public BoardDTO findById(@PathVariable Long boardId) {
        log.info("=============================================");
        log.info("게시판 상세보기 팝업 호출");
        log.info("=============================================");
        Board board = boardService.findById(boardId);

        if(board == null ){
            throw new NullPointerException("There are no entity by given id");
        }
        BoardDTO boardDTO = BoardDTO.builder()
                .boardId(board.getBoardId())
                .boardAsset(board.getBoardAsset())
                .boardTeacher(board.getBoardTeacher())
                .boardCarryInName(board.getBoardCarryInName())
                .boardAssetReturnYn(board.getBoardAssetReturnYn())
                .boardShareCount(board.getBoardShareCount())
                .boardRegDate(board.getBoardRegDate())
                .build();

        return boardDTO;
    }

    /**
     * 게시판 페이지 호출
     * @param principal
     * @param model
     * @return
     */
    @GetMapping(value = "/shareList")
    public String shareList (@RequestParam Map<String, Object> paramMap, Principal principal, Model model) {
        log.info("=============================================");
        log.info("전체 게시판 조회 ");
        log.info("=============================================");
        int page = paramMap.get("page") == null ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int pageUnit = paramMap.get("pageUnit") == null ? 10 : Integer.parseInt(paramMap.get("pageUnit").toString());

        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Board> allShareList = boardService.findAllShareList(page, pageUnit);
        int allBoardSize = boardService.findAll().size();

        //페이징 처리를 위한 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", allBoardSize > 10 ? allBoardSize%10 ==0 ? allBoardSize/10 : (allBoardSize/10)+1 : 1);
        model.addAttribute("pageUnit", pageUnit);

        model.addAttribute("teacherList", teachers);
        model.addAttribute("allBoardSize", allBoardSize);
        model.addAttribute("boards", allShareList);
        model.addAttribute("username", principal.getName());

        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());
        model.addAttribute("intro", teacher.getTeacherIntro());
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
        log.info("=============================================");
        log.info("반출 대장 등록하기");
        log.info("=============================================");
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
