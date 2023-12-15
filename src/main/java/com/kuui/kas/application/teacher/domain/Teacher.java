package com.kuui.kas.application.teacher.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    Long id;

    @Column(name = "login_id")
    String logInID;

    @Column(name = "login_pw")
    String logInPW;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "email_address")
    String emailAddress;

    @Column(name = "teacher_role")
    @Enumerated(EnumType.STRING)
    TeacherRole teacherRole;

    @Column(name = "name")
    String name;

    @Column(name = "christian_name")
    String christianName;

    @Column(name = "saints_day")
    String saintsDay;

    @Column(name = "teacher_insert_date")
    @CreationTimestamp
    LocalDateTime teacherInsertDate = LocalDateTime.now();
}
