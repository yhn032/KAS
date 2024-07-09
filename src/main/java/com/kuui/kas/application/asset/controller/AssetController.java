package com.kuui.kas.application.asset.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
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

    @GetMapping("/mainAsset")
    public String mainAsset(Principal principal, Model model){
        model.addAttribute("username", principal.getName());
        return "/asset/mainAsset";
    }

    @GetMapping("/allList")
    public String allList(@RequestParam Map<String, Object> paramMap, Principal principal, Model model){
        log.info("=============================================");
        log.info("전체 자산 조회 ");
        log.info("=============================================");
        String searchTerm = paramMap.get("searchTerm") == null || paramMap.get("searchTerm").equals("")  ? "" : paramMap.get("searchTerm").toString();
        int page = paramMap.get("page") == null ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int pageUnit = paramMap.get("pageUnit") == null ? 10 : Integer.parseInt(paramMap.get("pageUnit").toString());

        List<Asset> assetList = null;
        if(searchTerm.equals("")) {
            assetList = assetService.findAll(page, pageUnit);
        }else {
            assetList = assetService.searchAsset(searchTerm, page, pageUnit);
        }

        model.addAttribute("username", principal.getName());
        model.addAttribute("assetList", assetList);
        //페이징 처리를 위한 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", assetList.size() > 10 ? assetList.size() / 10 : 1);
        model.addAttribute("pageUnit", pageUnit);

        model.addAttribute("assetList", assetList);
        model.addAttribute("username", principal.getName());

        return "/asset/assetList";
    }

    @GetMapping("/addList")
    public String addAsset(Model model, Principal principal){
        //등록자 기록을 위해 모든 교사 정보 조회
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<String> names = teachers.stream().map(teacher -> teacher.getTeacherName()).collect(Collectors.toList());
        model.addAttribute("teacherNames", names);
        model.addAttribute("username", principal.getName());
        return "/asset/addAssetForm";
    }

    @PostMapping(value = "/addList", produces = "application/json")
    public ResponseEntity<?> handleFormUpload (
            @RequestPart(value = "assetImgFile", required = false)MultipartFile multipartFile, @RequestPart("assetDto") Asset asset, Principal principal) throws IOException, DuplicateNameAddException {

        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        System.out.println("asset = " + asset.getAssetName());
        System.out.println("asset.getRegTeacherName() = " + asset.getRegTeacherName());
        //자산 저장하기
        assetService.addAssetWithImage(asset, multipartFile, principal);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

//    @PostMapping(value = "/searchAsset")
//    @ResponseBody
//    public HashMap<String,Object> handleSearchResult(@RequestParam(value = "searchTerm")String searchTerm, @RequestParam Map<String, Object> paramMap, Principal principal, Model model) {
//        log.info("=============================================");
//        log.info("자산 검색 ");
//        log.info("=============================================");
//        int page = paramMap.get("page") == null ? 1 : Integer.parseInt(paramMap.get("page").toString());
//        int pageUnit = paramMap.get("pageUnit") == null ? 10 : Integer.parseInt(paramMap.get("pageUnit").toString());
//
//        HashMap<String,Object> resultMap = assetService.searchAsset(searchTerm, page, pageUnit);
//        int totalSize = (int) resultMap.get("totalSize");
//        if(totalSize > 0) {
//            resultMap.put("status", "success");
//        }else {
//            resultMap.put("status", "fail");
//        }
//        return resultMap;
//    }

    /**
     * 반출대장 입력시 자산 카테고리 찾기
     * @return
     */
    @GetMapping("/findCtg")
    @ResponseBody
    public List<String> findAssetCtg() {
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
        return assetService.findDataByCtg(ctg);
    }


    @GetMapping("/{assetId}/show")
    @ResponseBody
    public Asset findById(@PathVariable String assetId) {
        Asset asset = assetService.findById(assetId);

        if(asset == null) {
            throw new NullPointerException("There are no asset by given Id");
        }

        return asset;
    }
}
