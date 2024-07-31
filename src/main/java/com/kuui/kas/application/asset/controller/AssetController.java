package com.kuui.kas.application.asset.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.board.exception.NoRemainAssetException;
import com.kuui.kas.application.common.exception.DuplicateNameAddException;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/asset")
public class AssetController {
    private final AssetService assetService;
    private final TeacherService teacherService;

    @GetMapping("/allList")
    public String allList(@RequestParam Map<String, Object> paramMap, Principal principal, Model model){
        log.info("=============================================");
        log.info("전체 자산 조회 ");
        log.info("=============================================");
        String searchTerm = paramMap.get("searchTerm") == null || paramMap.get("searchTerm").equals("")  ? "" : paramMap.get("searchTerm").toString();
        int page = paramMap.get("page") == null ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int pageUnit = paramMap.get("pageUnit") == null ? 10 : Integer.parseInt(paramMap.get("pageUnit").toString());

        List<Asset> assetList = null;

        int totalSize = 0, realSize = 0;
        if(searchTerm.equals("")) {
            assetList = assetService.findAll(page, pageUnit);
            totalSize = assetService.findAll().size();
        }else {
            assetList = assetService.searchAsset(searchTerm, page, pageUnit);
            totalSize = assetList.size();
        }

        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());
        model.addAttribute("username", principal.getName());
        model.addAttribute("intro", teacher.getTeacherIntro());
        model.addAttribute("assetList", assetList);
        model.addAttribute("searchTerm", searchTerm);
        //페이징 처리를 위한 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalSize > 10 ? totalSize%10==0? totalSize / 10:(totalSize / 10)+1 : 1);
        model.addAttribute("pageUnit", pageUnit);

        model.addAttribute("assetList", assetList);
        model.addAttribute("username", principal.getName());

        return "/asset/assetList";
    }

    @GetMapping("/addList")
    public String addAsset(Model model, Principal principal){
        log.info("=============================================");
        log.info("자산 등록 폼 호출 ");
        log.info("=============================================");
        //등록자 기록을 위해 모든 교사 정보 조회
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<String> names = teachers.stream().map(teacher -> teacher.getTeacherName()).collect(Collectors.toList());
        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());
        model.addAttribute("username", principal.getName());
        model.addAttribute("intro", teacher.getTeacherIntro());
        model.addAttribute("teacherNames", names);
        return "/asset/addAssetForm";
    }

    @PostMapping(value = "/addList", produces = "application/json")
    public ResponseEntity<?> handleFormUpload (
            @RequestPart(value = "assetImgFile", required = false)MultipartFile[] multipartFiles, @RequestPart("assetDto") Asset asset, Principal principal) throws IOException, DuplicateNameAddException {
        log.info("AssetController.handleFormUpload");
        log.info("=============================================");
        log.info("자산 이미지 들어옴");
        log.info("=============================================");
        if (multipartFiles.length > 3 ) {
            return new ResponseEntity<>("You can upload up to 3 images only", HttpStatus.BAD_REQUEST);
        }
        //자산 저장하기
        assetService.addAssetWithImage(asset, multipartFiles, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 반출대장 입력시 자산 카테고리 찾기
     * @return
     */
    @GetMapping("/findCtg")
    @ResponseBody
    public List<String> findAssetCtg() {
        log.info("=============================================");
        log.info("자산 카테고리 조회 ");
        log.info("=============================================");
        return assetService.findAllCtg();
    }

    /**
     * 카테고리에 해당하는 데이터 가져오기
     * @param ctg
     * @return
     */
    @GetMapping("/dtlData")
    @ResponseBody
    public List<Asset> findDataByCtg(@RequestParam String ctg) {
        log.info("=============================================");
        log.info("자산 카테고리에 해당하는 자산 조회 ");
        log.info("=============================================");
        return assetService.findDataByCtg(ctg);
    }


    /**
     * 자산 상세보기 호출
     * @param assetId
     * @return
     */
    @GetMapping("/{assetId}/show")
    @ResponseBody
    public Asset findById(@PathVariable String assetId) {
        log.info("=============================================");
        log.info("자산 상세보기용 자산 데이터 호출 ");
        log.info("=============================================");
        Asset asset = assetService.findById(assetId);

        if(asset == null) {
            throw new NullPointerException("There are no asset by given Id");
        }

        return asset;
    }

    /**
     * 자산 삭제 기능 비동기 호출
     */
    @GetMapping("/{assetId}/delete")
    public ResponseEntity<?> deleteAsset(@PathVariable String assetId){
        log.info("=============================================");
        log.info("자산 삭제 로직 호출 ");
        log.info("=============================================");
        Long deleteAsset = assetService.deleteAsset(assetId);

        if(deleteAsset == 0 ) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 자산 수정 폼 호출
     */
    @GetMapping("/{assetId}/modify")
    public String modifyAssetForm(@PathVariable String assetId, Model model, Principal principal) {
        log.info("=============================================");
        log.info("자산 수정 폼 호출 ");
        log.info("=============================================");

        Asset asset = assetService.findById(assetId);
        List<Teacher> allTeachers = teacherService.findAllTeachers();
        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());
        model.addAttribute("username", principal.getName());
        model.addAttribute("intro", teacher.getTeacherIntro());
        model.addAttribute("asset", asset);
        model.addAttribute("teachers", allTeachers);

        return "/asset/modifyAssetForm";
    }

    @PostMapping(value = "/modify", produces = "application/json")
    public ResponseEntity<?> handleFormModify (
            @RequestPart(value = "assetImgFile", required = false)MultipartFile[] multipartFiles, @RequestPart("assetDto") Asset asset, Principal principal) throws IOException, DuplicateNameAddException, NoRemainAssetException {
        log.info("AssetController.handleFormModify");
        log.info("=============================================");
        log.info("자산 수정");
        log.info("=============================================");
        if (multipartFiles.length > 3 ) {
            return new ResponseEntity<>("You can upload up to 3 images only", HttpStatus.BAD_REQUEST);
        }

        //자산 저장하기
        assetService.modifyAssetWithImage(asset, multipartFiles, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("excelUpload") MultipartFile file) {
        try{

            assetService.uploadBulkExcel(file);
            return new ResponseEntity<>("File uploaded and data saved successfully!", HttpStatus.OK);
        }catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occured while processing file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
