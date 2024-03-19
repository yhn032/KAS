package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService implements UserDetailsService {
    private final TeacherRepository teacherRepository;
    private final FileService fileService;

    @Transactional
    public Teacher saveTeacher(Teacher teacher) {
        validateDuplicateTeacher(teacher);
        return teacherRepository.save(teacher);
    }

    public void validDuplicateID(String id){
        Teacher teacher = teacherRepository.findByLoginId(id);
        if(teacher != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
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

    public List<Teacher> findAllTeachers(){return teacherRepository.findAll();}

    @Transactional(rollbackFor = Exception.class)
    public SaveFile addProfileImgOnFileSystem(MultipartFile multipartFile, Principal principal) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalFilename;
        String fileExt = FileUtil.getExtension(originalFilename);
        String uploadPath = "D:\\KasImg\\profile";
        Asset asset = new Asset();

        //파일 정보 DB에 저장
        SaveFile saveFile = SaveFile.builder()
                .orgFileName(originalFilename)
                .saveName(savedName)
                .asset(asset)
                .filePath(uploadPath)
                .fileType(fileExt)
                .uploadUser(principal.getName())
                .fileSize(multipartFile.getSize())
                .build();

        fileService.saveFile(FileDto.from(saveFile));
        multipartFile.transferTo(new File(uploadPath, savedName));

        return saveFile;
    }
}
