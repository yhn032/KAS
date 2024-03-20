package com.kuui.kas.application.teacher.controller;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/duplicateId")
    @ResponseBody
    public String duplicateId(String id){

        String result = "";
        try{
            teacherService.validDuplicateID(id);
            result = "success";
        }catch (IllegalStateException e){
            e.printStackTrace();
            result = "error";
        }

        return result;
    }

    @GetMapping("/getTeacherProfile")
    @ResponseBody
    public String getTeacherProfile(Principal principal) {
        return teacherService.findByTeacherNickName(principal.getName()).getTeacherProfileImg().getSaveName();
    }


    @GetMapping("/manage")
    public String manageTeacherList(Principal principal, Model model){
        List<Teacher> allTeachers = teacherService.findAllTeachers();
        model.addAttribute("username", principal.getName());
        model.addAttribute("teachers", allTeachers);
        return "teacher/allTeacherList";
    }

    @PostMapping("/addProfileImg")
    @ResponseBody
    public Map<String, String> addProfileImg(
            @RequestPart(value = "teacherProfileImg", required = false)MultipartFile multipartFile
            , Principal principal
            ) throws IOException {
        HashMap<String, String> resultMap = new HashMap<>();

        SaveFile saveFile = teacherService.addProfileImgOnFileSystem(multipartFile, principal);

        resultMap.put("result", "success");
        resultMap.put("orgName", saveFile.getOrgFileName());
        resultMap.put("savedName", saveFile.getSaveName());
        System.out.println("resultMap = " + resultMap.get("orgName"));
        System.out.println("resultMap = " + resultMap.get("savedName"));

        return  resultMap;
    }

    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model){
        log.info("마이페이지 온 로딩");

        log.info("로그인한 사용자의 닉네임으로 teacher 가져오기");
        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());

        model.addAttribute("username", principal.getName());
        model.addAttribute("teacherInfo", teacher);
        log.info("정적 페이지(myPage) 포워딩 ");
        return "teacher/myPage";
    }

    @GetMapping("/modifyInfo")
    public String modifyInfo(Principal principal, Model model){
        log.info("수정 버튼 클릭. 수정폼 호출하기");
        Teacher teacher = teacherService.findByTeacherNickName(principal.getName());

        model.addAttribute("username", principal.getName());
        model.addAttribute("teacherInfo", teacher);
        return "teacher/modifyTeacherInfo";
    }

    @PostMapping("/modifyTeacherInfo")
    @ResponseBody
    public String modifyTeacherInfo(
            @RequestPart(value = "profileImgFile") MultipartFile profileImg,
            @RequestPart(value = "teacherDto")TeacherFormDto teacherFormDto,
            Model model
            ) throws IOException {
        log.info("변경한 데이터 서버에서 수신 완료");
        teacherService.modifyTeacherInfo(profileImg, teacherFormDto);

        log.info("마이페이지로 리다이렉트");
        return "success";
    }
}
