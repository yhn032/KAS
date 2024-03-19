package com.kuui.kas.application.teacher.controller;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/manage")
    public String manageTeacherList(Principal principal, Model model){
        List<Teacher> allTeachers = teacherService.findAllTeachers();
        model.addAttribute("username", principal.getName());
        model.addAttribute("teachers", allTeachers);
        return "/teacher/allTeacherList";
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

}
