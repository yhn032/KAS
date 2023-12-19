package com.kuui.kas.application.teacher.repository;

import com.kuui.kas.application.teacher.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    //null인경우 가능 값이 있으면 중복
    Teacher findByEmail(String email);

    Teacher findByLoginId(String id);
}
