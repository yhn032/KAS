package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.dto.TeacherFormDto;
import com.kuui.kas.application.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService implements UserDetailsService {
    private final TeacherRepository teacherRepository;
    private final FileService fileService;

    public Teacher findById(Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.get();
    }

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
//                .asset(asset)
                .filePath(uploadPath)
                .fileType(fileExt)
                .uploadUser(principal.getName())
                .fileSize(multipartFile.getSize())
                .build();

        fileService.saveFile(FileDto.from(saveFile));
        multipartFile.transferTo(new File(uploadPath, savedName));

        return saveFile;
    }

    public Teacher findByTeacherNickName(String name) {
        return teacherRepository.findByTeacherNickName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyTeacherInfo(MultipartFile profileImg,TeacherFormDto teacherFormDto) throws IOException {
        //DB데이터 변경 감지용 수정
        Teacher teacher = teacherRepository.findByLoginId(teacherFormDto.getTeacherLogInID());
        teacher.setTeacherName(teacherFormDto.getTeacherName());
        teacher.setTeacherNickname(teacherFormDto.getTeacherNickname());
        teacher.setTeacherPhoneNumber(teacherFormDto.getTeacherPhoneNumber());
        teacher.setTeacherEmailAddress(teacherFormDto.getTeacherEmailAddress());
        teacher.setTeacherChristianName(teacherFormDto.getTeacherChristianName());
        teacher.setTeacherSaintsDay(teacherFormDto.getTeacherSaintsDay());

        if(!profileImg.isEmpty()) {
            log.info("신규 프로필 이미지 넘어옴");
            //현재 로그인한 사용자의 프로필이 기본 이미지가 아닌지 확인.
            //기본 이미지라면 신규 등록, 아니라면 이미지 변경이다.
            SaveFile img = teacher.getTeacherProfileImg();
            System.out.println("img.getOrgFileName() = " + img.getOrgFileName());

            if(!img.getOrgFileName().equals("default-profile.jpg")) {//기본이미지에서 수정이라면 그냥 파일만 업로드
                //파일 변경이므로 디비데이터는 그냥 수정하면 되지만 파일시스템에 기존에 있던 이미지는 삭제하고 새로 업로드 한다.
                String imgPath = img.getFilePath() + "\\" + img.getSaveName();
                Path imagePath = Paths.get(imgPath);

                try {
                    Files.deleteIfExists(imagePath);
                    log.info("이미지 파일이 파일 시스템에서 성공적으로 삭제되었습니다.");
                } catch (IOException e) {
                    log.info("이미지 파일을 삭제하는 중에 오류가 발생했습니다.");
                    e.printStackTrace();
                }
            }

            //DB데이터 변경 감지용 수정
            img.setOrgFileName(profileImg.getOriginalFilename());
            img.setSaveName(UUID.randomUUID() + "_" + profileImg.getOriginalFilename());
            img.setFileType(FileUtil.getExtension(profileImg.getOriginalFilename()));
            img.setUploadUser(teacher.getTeacherName());
            img.setFileSize(profileImg.getSize());

            log.info("신규 이미지 업로드");
            //첨부파일 업로드 시작

            log.info("파일 시스템에 이미지 저장");
            profileImg.transferTo(new File(img.getFilePath(), img.getSaveName()));
        }
    }
}
