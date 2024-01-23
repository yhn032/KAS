package com.kuui.kas.application.common.controller;

import com.kuui.kas.application.common.service.CommonService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;

    @GetMapping("/common/login")
    public String login(){
        return "common/login";
    }

    @GetMapping("/common/signup")
    public String signUp(){
        return "common/signup";
    }

    @GetMapping("/common/dashboard")
    public String dashboard(Principal principal, Model model, Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Teacher teacher = teacherService.findByLoginId(user.getUsername());
        System.out.println("principal.getName() = " + principal.getName());
        System.out.println("teacher.getTeacherNickname() = " + teacher.getTeacherNickname());
        System.out.println("teacher.getTeacherRole() = " + teacher.getTeacherRole());
        
        model.addAttribute("username", teacher.getTeacherNickname());
        return "common/dashboard";
    }

    @PostMapping("/common/signup")
    public String createTeacher(Teacher teacher){
        TeacherFormDto teacherFormDto = TeacherFormDto.builder()
                .teacherLogInID(teacher.getTeacherLogInID())
                .teacherLogInPW(teacher.getTeacherLogInPW())
                .teacherPhoneNumber(teacher.getTeacherPhoneNumber())
                .teacherNickname(teacher.getTeacherNickname())
                .teacherEmailAddress(teacher.getTeacherEmailAddress())
                .teacherRole(TeacherRole.USER)
                .teacherName(teacher.getTeacherNickname())
                .teacherChristianName(teacher.getTeacherChristianName())
                .teacherSaintsDay(teacher.getTeacherSaintsDay())
                .teacherInsertDate(LocalDateTime.now())
                .build();

        Teacher buildTeacher = Teacher.createTeacher(teacherFormDto, passwordEncoder);
        Teacher savedTeacher = teacherService.saveTeacher(buildTeacher);
        return "redirect:/login";
    }
}
