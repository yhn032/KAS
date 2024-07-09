package com.kuui.kas.application.common.controller;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
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

import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;
    private final FileService fileService;

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
        model.addAttribute("username", principal.getName());
        return "common/dashboard";
    }

    @PostMapping("/common/signup")
    public String createTeacher(Teacher teacher){
        //최초 회원가입 시 기본 이미지 가져와서 저장하기
        String imgPath = "D:\\KasImg\\profile";
        String fileName = "default-profile.jpg";

        SaveFile bySaveName = fileService.findBySaveName(fileName);
        SaveFile teacherProfileFile;

        //기본 이미지가 없다면 새로 추가. 있다면 기존거 가져와서 생성
        if(bySaveName == null ){
            File file = FileUtil.createFile(imgPath, fileName);

            SaveFile saveFile = SaveFile.builder()
                    .orgFileName(fileName)
                    .saveName(fileName)
//                    .asset(null)
                    .filePath(imgPath)
                    .fileType(FileUtil.getExtension(fileName))
                    .uploadUser("SUPER")
                    .fileSize(file.length())
//                .teacher(savedTeacher)
                    .build();

            FileDto fileDto = fileService.saveFile(FileDto.from(saveFile));
            teacherProfileFile = fileService.findById(fileDto.getId());
        }else {
            teacherProfileFile = bySaveName;
        }


        TeacherFormDto teacherFormDto = TeacherFormDto.builder()
                .teacherLogInID(teacher.getTeacherLogInID())
                .teacherLogInPW(teacher.getTeacherLogInPW())
                .teacherPhoneNumber(teacher.getTeacherPhoneNumber())
                .teacherNickname(teacher.getTeacherNickname())
                .teacherEmailAddress(teacher.getTeacherEmailAddress())
                .teacherRole(TeacherRole.USER)
                .teacherName(teacher.getTeacherName())
                .teacherChristianName(teacher.getTeacherChristianName())
                .teacherSaintsDay(teacher.getTeacherSaintsDay())
                .teacherInsertDate(LocalDateTime.now())
                .teacherProfileImg(teacherProfileFile)
                .build();

        Teacher buildTeacher = Teacher.createTeacher(teacherFormDto, passwordEncoder);
        Teacher savedTeacher = teacherService.saveTeacher(buildTeacher);

        return "redirect:/login";
    }
}
