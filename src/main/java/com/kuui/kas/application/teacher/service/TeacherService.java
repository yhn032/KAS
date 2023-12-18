package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public Teacher saveTeacher(Teacher teacher) {
        validateDuplicateTeacher(teacher);
        return teacherRepository.save(teacher);
    }

    private void validateDuplicateTeacher(Teacher teacher) {
        Teacher findTeacher = teacherRepository.findByEmail(teacher.getTeacherEmailAddress());
        if(findTeacher != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
