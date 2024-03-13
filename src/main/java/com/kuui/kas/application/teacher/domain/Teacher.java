package com.kuui.kas.application.teacher.domain;

import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    Long teacherId;

    @Column(name = "teacher_login_id")
    String teacherLogInID;

    @Column(name = "teacher_login_pw")
    String teacherLogInPW;

    @Column(name = "teacher_phone_number")
    String teacherPhoneNumber;

    @Column(name = "teacher_nickname")
    String teacherNickname;

    @Column(name = "teacher_email_address")
    String teacherEmailAddress;

    @Column(name = "teacher_role")
    @Enumerated(EnumType.STRING)
    TeacherRole teacherRole;

    @Column(name = "teacher_name")
    String teacherName;

    @Column(name = "teacher_christian_name")
    String teacherChristianName;

    @Column(name = "teacher_saints_day")
    String teacherSaintsDay;

    @Column(name = "teacher_insert_date")
    @CreationTimestamp
    LocalDateTime teacherInsertDate = LocalDateTime.now();

    @Column(name = "teacher_profile_img")
    String teacherProfileImg;

    @Builder
    public Teacher(String teacherLogInID, String teacherLogInPW, String teacherPhoneNumber, String teacherNickname, String teacherEmailAddress, TeacherRole teacherRole, String teacherName, String teacherChristianName, String teacherSaintsDay, LocalDateTime teacherInsertDate, String teacherProfileImg) {
        this.teacherLogInID = teacherLogInID;
        this.teacherLogInPW = teacherLogInPW;
        this.teacherPhoneNumber = teacherPhoneNumber;
        this.teacherNickname = teacherNickname;
        this.teacherEmailAddress = teacherEmailAddress;
        this.teacherRole = teacherRole;
        this.teacherName = teacherName;
        this.teacherChristianName = teacherChristianName;
        this.teacherSaintsDay = teacherSaintsDay;
        this.teacherInsertDate = teacherInsertDate;
        this.teacherProfileImg = teacherProfileImg;
    }

    public static Teacher createTeacher(TeacherFormDto teacherFormDto, PasswordEncoder passwordEncoder){
        Teacher teacher = Teacher.builder()
                .teacherLogInID(teacherFormDto.getTeacherLogInID())
                .teacherLogInPW(passwordEncoder.encode(teacherFormDto.getTeacherLogInPW()))
                .teacherPhoneNumber(teacherFormDto.getTeacherPhoneNumber())
                .teacherNickname(teacherFormDto.getTeacherNickname())
                .teacherEmailAddress(teacherFormDto.getTeacherEmailAddress())
                .teacherRole(teacherFormDto.getTeacherRole())
                .teacherName(teacherFormDto.getTeacherName())
                .teacherChristianName(teacherFormDto.getTeacherChristianName())
                .teacherSaintsDay(teacherFormDto.getTeacherSaintsDay())
                .teacherInsertDate(teacherFormDto.getTeacherInsertDate())
                .teacherProfileImg(teacherFormDto.getTeacherProfileImg())
                .build();

        return teacher;
    }
}
