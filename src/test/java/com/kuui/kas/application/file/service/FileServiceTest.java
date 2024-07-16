package com.kuui.kas.application.file.service;

import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.repository.SaveFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private SaveFileRepository saveFileRepository;

    @InjectMocks
    private FileService fileService;

    @Value("${com.kuui.kas.path.assetImg}")
    private static String uploadPath;

    @Test
    public void testSaveFileSuccess() throws Exception {
        SaveFile saveFile1 = SaveFile.builder()
                .orgFileName("testImg1")
                .saveName(UUID.randomUUID().toString() + "testImg1")
                .filePath(uploadPath)
                .fileType("png")
                .uploadUser("김병국")
                .fileSize(10460L)
                .build();

        SaveFile saveFile2 = SaveFile.builder()
                .orgFileName("testImg2")
                .saveName(UUID.randomUUID().toString() + "testImg2")
                .filePath(uploadPath)
                .fileType("jpg")
                .uploadUser("김병삼")
                .fileSize(10460L)
                .build();

    }
}