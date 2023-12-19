package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeacherServiceTest {
    @Autowired TeacherService teacherService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    public void saveTeacherTest(){
        Teacher teacher = createTeacher();
        Teacher savedTeacher = teacherService.saveTeacher(teacher);

        assertEquals(teacher.getTeacherEmailAddress(), savedTeacher.getTeacherEmailAddress());
    }

    @Test
    @DisplayName("중복가입 테스트")
    public void duplicateTest(){
        Teacher teacher = createTeacher();
        Teacher teacher2 = createTeacher2();
        Teacher savedTeacher = teacherService.saveTeacher(teacher);
        Teacher savedTeacher2 = teacherService.saveTeacher(teacher2);

        assertEquals(teacher.getTeacherEmailAddress(), savedTeacher.getTeacherEmailAddress());
    }

    public Teacher createTeacher(){
        TeacherFormDto build = TeacherFormDto.builder()
                .teacherLogInID("yhn032")
                .teacherLogInPW("kaskas123!!")
                .teacherPhoneNumber("010-1234-5678")
                .teacherNickname("leo")
                .teacherEmailAddress("kas123@naver.com")
                .teacherRole(TeacherRole.USER)
                .teacherName("뱅국")
                .teacherChristianName("레오")
                .teacherSaintsDay("04.19")
                .teacherInsertDate(LocalDateTime.now())
                .build();
        return Teacher.createTeacher(build, passwordEncoder);
    }

    public Teacher createTeacher2(){
        TeacherFormDto build = TeacherFormDto.builder()
                .teacherLogInID("yhn032")
                .teacherLogInPW("kaskas123!!")
                .teacherPhoneNumber("010-1234-5678")
                .teacherNickname("leo")
                .teacherEmailAddress("kas123@naver.com")
                .teacherRole(TeacherRole.USER)
                .teacherName("뱅국")
                .teacherChristianName("레오")
                .teacherSaintsDay("04.19")
                .teacherInsertDate(LocalDateTime.now())
                .build();
        return Teacher.createTeacher(build, passwordEncoder);
    }
}