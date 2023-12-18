package com.kuui.kas.application.common.controller;

import com.kuui.kas.application.common.service.CommonService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;

    @GetMapping("/login")
    public String login(){
        return "common/login";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "common/signup";
    }

    @PostMapping("/signup")
    public String createTeacher(Teacher teacher){
//        System.out.println("teacher.getTeacherLogInID() = " + teacher.getTeacherLogInID());
//        System.out.println("teacher.getTeacherLogInPW() = " + teacher.getTeacherLogInPW());
//        System.out.println("teacher.getTeacherPhoneNumber() = " + teacher.getTeacherPhoneNumber());
//        System.out.println("teacher.getTeacherNickname() = " + teacher.getTeacherNickname());
//        System.out.println("teacher.getTeacherEmailAddress() = " + teacher.getTeacherEmailAddress());
//        System.out.println("teacher.getTeacherRole() = " + teacher.getTeacherRole());
//        System.out.println("teacher.getTeacherName() = " + teacher.getTeacherName());
//        System.out.println("teacher.getTeacherChristianName() = " + teacher.getTeacherChristianName());
//        System.out.println("teacher.getTeacherSaintsDay() = " + teacher.getTeacherSaintsDay());
        TeacherFormDto teacherFormDto = TeacherFormDto.builder()
                .teacherLogInID(teacher.getTeacherLogInID())
                .teacherLogInPW(teacher.getTeacherLogInPW())
                .teacherPhoneNumber(teacher.getTeacherPhoneNumber())
                .teacherNickname(teacher.getTeacherNickname())
                .teacherEmailAddress(teacher.getTeacherEmailAddress())
                .teacherRole(TeacherRole.ROLE_USER)
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
