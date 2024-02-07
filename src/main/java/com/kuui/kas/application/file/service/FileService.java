package com.kuui.kas.application.file.service;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.repository.SaveFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {

    public final SaveFileRepository fileRepository;

    public FileDto getFile(Long fileId) {
        log.info("get File() fileId : {}", fileId);
        return fileRepository.findById(fileId).map(FileDto::from).orElseThrow(() -> new EntityNotFoundException("파일이 없습니다 - fileId : " + fileId));
    }

    public void deleteFile(Long fileId) {
        log.info("deleteFile() fileId : {}", fileId);
        fileRepository.deleteById(fileId);
    }

    public FileDto saveFile(FileDto saveFile) {
        log.info("saveFile() saveFile : {}", saveFile);
        return FileDto.from(fileRepository.save(saveFile.toEntity()));
    }
}

