package com.kuui.kas.application.common.controller;

import com.kuui.kas.application.common.service.CommonService;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommonController {
//    private final PasswordEncoder passwordEncoder;
//    private final CommonService commonService;

//    @GetMapping("/login")
//    public String login(){
//        return "teacher/login";
//    }
//
//    @GetMapping("/signUp")
//    public String signUp(){
//        return "teacher/signUp";
//    }
//
//    @PostMapping("/signUp")
//    public String createTeacher(Teacher teacher){
//        teacher.setLogInPW(passwordEncoder.encode(teacher.getLogInPW()));
//        commonService.save(teacher);
//
//        return "redirect:/login";
//    }
}
