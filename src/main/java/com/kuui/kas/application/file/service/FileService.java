package com.kuui.kas.application.file.service;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.repository.SaveFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {

    public final SaveFileRepository fileRepository;

    @Value("${com.kuui.kas.path.assetImg}")
    private static String uploadPath;

    public FileDto getFile(Long fileId) {
        log.info("get File() fileId : {}", fileId);
        return fileRepository.findById(fileId).map(FileDto::from).orElseThrow(() -> new EntityNotFoundException("파일이 없습니다 - fileId : " + fileId));
    }

    public void deleteFile(Long fileId) {
        log.info("deleteFile() fileId : {}", fileId);
        fileRepository.deleteById(fileId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveImgFile(List<SaveFile> imgFiles, MultipartFile[] multipartFiles) throws IOException {
        log.info("saveFile() saveFile : {}", imgFiles);
        List<Path> savedPaths = new ArrayList<>();
        try{
            for(int i=0; i<imgFiles.size(); i++) {
                //Save file to Disk
                Path path = Paths.get(uploadPath + multipartFiles[i].getOriginalFilename());
                multipartFiles[i].transferTo(new File(uploadPath, imgFiles.get(i).getSaveName()));
                savedPaths.add(path);
            }
            //Save imgFile information to database
            for(SaveFile imgFile : imgFiles) {
                fileRepository.save(imgFile);
            }
        }catch (RuntimeException | IOException e) {
            //rollback transaction and delete file
            for (Path path : savedPaths) {
                try{
                    Files.deleteIfExists(path);
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // Re-throw the original exception to trigger transaction rollback
            throw e;
        }

    }
    public FileDto saveFile(FileDto saveFile) throws IOException {
        log.info("saveFile() saveFile : {}", saveFile);
        return FileDto.from(fileRepository.save(saveFile.toEntity()));
    }

    public SaveFile findById(Long fileId) {
        return fileRepository.findById(fileId).get();
    }

    public SaveFile findBySaveName(String saveName) {
        return  fileRepository.findBySaveName(saveName);
    }
}

