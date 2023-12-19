package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherRepository.findByLoginId(username); //username -> 로그인에 사용한 값(이메일 혹은 아이디)
        if(teacher == null){
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(teacher.getTeacherNickname())
                .password(teacher.getTeacherLogInPW())
                .roles(teacher.getTeacherRole().toString())
                .build();
    }

    public Teacher findByLoginId(String id){return teacherRepository.findByLoginId(id);}
}
