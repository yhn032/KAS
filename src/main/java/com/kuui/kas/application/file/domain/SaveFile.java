package com.kuui.kas.application.file.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.common.domain.Auditing;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ToString
public class SaveFile extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fild_id")
    private Long id;

    @Setter
    @Column(name = "original_file_name", nullable = false)
    private String orgFileName; //원본 이름

    @Setter
    @Column(name = "save_name", nullable = false)
    private String saveName; //저장되는 이름

    @Setter
    @Column(name = "file_path",nullable = false)
    private String filePath ;

    @Setter
    @Column(name = "file_type",nullable = false)
    private String fileType;

    @Setter
    @Column(name = "file_size",nullable = false)
    private Long fileSize;

    @Setter
    @Column(name = "upload_user", nullable = false)
    private String uploadUser;
}

/*
* @AllArgsConstructor(access = AccessLevel.PRIVATE)
* - 클래스의 모든 필드를 파라미터로 받는 생성자를 자동으로 생성한다.
* - AccessLevel.PRIVATE을 사용하면 생성자가 외부에서 호출될 수 없고, 해당 클래스 내부에서만 호출할 수 있다.
* - create라는 정적 팩토리 메소드를 통해서만 객체를 생성할 수 있다.
* */