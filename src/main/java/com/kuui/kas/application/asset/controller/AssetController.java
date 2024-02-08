package com.kuui.kas.application.asset.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.common.exception.DuplicateNameAddException;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/asset")
public class AssetController {
    private final AssetService assetService;
    private final TeacherService teacherService;

    @GetMapping("/mainAsset")
    public String mainAsset(Principal principal, Model model, Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        model.addAttribute("username", principal.getName());
        return "/asset/mainAsset";
    }

    @GetMapping("/allList")
    public String allList(Principal principal, Model model, Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        model.addAttribute("username", principal.getName());

        List<Asset> assetList = assetService.findAll();
        model.addAttribute("assetList", assetList);

        return "/asset/assetList";
    }

    @GetMapping("/addList")
    public String addAsset(Model model){
        //모든 교사 정보 조회
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<String> names = teachers.stream().map(teacher -> teacher.getTeacherName()).collect(Collectors.toList());
        model.addAttribute("teacherNames", names);
        return "/asset/addAssetForm";
    }

    @PostMapping(value = "/addTestList", produces = "application/json")
    @ResponseBody
    //public String addAssetFromForm(@RequestParam("assetName")String assetName, @RequestParam("assetCnt")Long assetCnt, @RequestParam("assetPos")int assetPos, @RequestParam("regTeacherName")String regTeacherName, Model model){
    public Map<String, String> addAssetFromForm(@RequestBody Asset asset) throws DuplicateNameAddException {
        //동일한 이름이 이미 존재한다면 수량을 수정하도록 익셉션 터트리기
        assetService.duplicateAssetName(asset.getAssetName());
        //예외가 터지지 않고 진행되면 문제 없는 거임

        //마지막 물품 번호 조회하기 -> 등록 날짜 순 으로 조회해서 가장 최신에 등록된 상품 가져오기
        String assetNo =assetService.LastAssetNo();
        int num = Integer.parseInt(assetNo.substring(assetNo.indexOf("-") + 1, assetNo.length()));

        Asset newAsset = new Asset(UUID.randomUUID().toString(), "kuui-" + (++num), asset.getAssetName(), asset.getAssetCnt(), asset.getAssetPos(), asset.getRegTeacherName(), asset.getRegTeacherName());

        //물품 저장하기 -> 저장하려는 이름이 이미 있다면 exception을 터트려서 이미 등록되어 있으니 수량을 수정하라고 알려주기
        assetService.saveAsset(newAsset);

//        String assetIdFindByAssetNo = assetService.getAssetIdFindByAssetNo("kuui-" + num);

        Map<String, String> responseData =  new HashMap<>();
        responseData.put("status", "200");
        responseData.put("message", "재고 상품이 성공적으로 추가되었습니다.");
//        responseData.put("assetId", assetIdFindByAssetNo);

        return responseData;
    }

    @PostMapping(value = "/addList", produces = "application/json")
    public ResponseEntity<?> handleFormUpload (
            @RequestPart(value = "assetImgFile", required = false)MultipartFile multipartFile, @RequestPart("assetDto") Asset asset, Principal principal) throws IOException, DuplicateNameAddException {

        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        System.out.println("asset = " + asset.getAssetName());

        //자산 저장하기
        assetService.addAssetWithImage(asset, multipartFile, principal);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
