package com.kuui.kas.application.teacher.dto;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TeacherFormDto {

    Long teacherId;

    String teacherLogInID;

    String teacherLogInPW;

    String teacherPhoneNumber;

    String teacherNickname;

    String teacherEmailAddress;

    @Enumerated(EnumType.STRING)
    TeacherRole teacherRole;

    String teacherName;

    String teacherChristianName;

    String teacherSaintsDay;

    LocalDateTime teacherInsertDate = LocalDateTime.now();

    SaveFile teacherProfileImg;

    @Builder
    public TeacherFormDto(String teacherLogInID, String teacherLogInPW, String teacherPhoneNumber, String teacherNickname, String teacherEmailAddress, TeacherRole teacherRole, String teacherName, String teacherChristianName, String teacherSaintsDay, LocalDateTime teacherInsertDate) {
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
    }
}
