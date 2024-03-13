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
}
