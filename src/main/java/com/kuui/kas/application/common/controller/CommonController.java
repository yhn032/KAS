package com.kuui.kas.application.common.controller;

import com.kuui.kas.application.common.service.CommonService;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;
    private final FileService fileService;
    private final CommonService commonService;

    @GetMapping("/login")
    public String login(){
        return "common/login";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "common/signup";
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model, Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        model.addAttribute("username", principal.getName());
        return "common/dashboard";
    }

    @PostMapping("/signup")
    public String createTeacher(Teacher teacher) throws IOException {
        log.info("=============================================");
        log.info("회원가입 시도");
        log.info("=============================================");
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
                    .filePath(imgPath)
                    .fileType(FileUtil.getExtension(fileName))
                    .uploadUser("SUPER")
                    .fileSize(file.length())
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
        teacherService.saveTeacher(buildTeacher);

        return "redirect:/login";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) {
        commonService.export(response);
    }
}
