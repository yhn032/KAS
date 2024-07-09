package com.kuui.kas.application.file.dto;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Builder
@Getter
public class FileDto {
    private final Long id;
    private final String orgFileName;
    private final String saveName;
    private final String filePath;
    private final String fileType;
    private final Long fileSize;
    private final String uploadUser;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime updatedAt;
    private final String updatedBy;
//    private final Asset asset;
//    private final Teacher teacher;

    // 정적 팩토리 메서드
    public static FileDto from(SaveFile saveFile) {
        return new FileDto(
                saveFile.getId(),
                saveFile.getOrgFileName(),
                saveFile.getSaveName(),
                saveFile.getFilePath(),
                saveFile.getFileType(),
                saveFile.getFileSize(),
                saveFile.getUploadUser(),
                saveFile.getCreatedDate(),
                saveFile.getCreatedName(),
                saveFile.getUpdatedDate(),
                saveFile.getUpdatedName()
//                saveFile.getAsset(),
//                saveFile.getTeacher()
        );
    }

    public SaveFile toEntity(){
        return SaveFile.builder()
                .id(id)
                .orgFileName(orgFileName)
                .saveName(saveName)
                .filePath(filePath)
                .fileType(fileType)
                .fileSize(fileSize)
                .uploadUser(uploadUser)
//                .asset(asset)
//                .teacher(teacher)
                .build();
    }
}
