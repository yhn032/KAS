package com.kuui.kas.application.asset.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        List<Asset> assetList = assetService.allAssetList();
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

    @PostMapping("addList")
    public String addAssetFromForm(@RequestParam("assetName")String assetName, @RequestParam("assetCnt")Long assetCnt, @RequestParam("regTeacherName")String regTeacherName, Model model){
        //마지막 물품 번호 조회하기 -> 등록 날짜 순 으로 조회해서 가장 최신에 등록된 상품 가져오기
        String assetNo =assetService.LastAssetNo();
        int num = Integer.parseInt(assetNo.substring(assetNo.indexOf("-") + 1, assetNo.length()));

        Asset newAsset = new Asset(UUID.randomUUID().toString(), "kuui-" + (++num), assetName, assetCnt, regTeacherName, regTeacherName);

        //물품 저장하기 -> 저장하려는 이름이 이미 있다면 exception을 터트려서 이미 등록되어 있으니 수량을 수정하라고 알려주기
        assetService.saveAsset(newAsset);

        List<Asset> assetList = assetService.allAssetList();
        model.addAttribute("assetList", assetList);
        return "/asset/assetList";
    }
}
