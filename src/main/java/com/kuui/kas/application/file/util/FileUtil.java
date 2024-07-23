package com.kuui.kas.application.file.util;

import com.kuui.kas.application.file.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class FileUtil {
    private FileUtil(){}

    public static String uploadPath = "D:\\KasImg\\asset\\";

    //확장자를 가져오는 메소드
    public static String getExtension(String fileName){
        return fileName.substring(fileName.indexOf(".")+1);
    }

    //파일 이름만을 가져오는 메소드
    public static String getFileNameOnly(String fileName){
        return fileName.substring(0, fileName.indexOf("."));
    }

    //실제 저장될 파일 이름
    public static String getFileNameWithUUID(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    //정적 팩토리 메소드
    public static File createFile(String uploadPath, String fileName) {
        return new File(uploadPath, fileName);
    }

    public static File getMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(uploadPath, getFileNameWithUUID(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(file);
        return file;
    }

    public static File getFileFromFileDomain(FileDto fileDto) {
        return new File(fileDto.getFilePath());
    }

    public static void deleteFile(FileDto assetImg) {
        File file = getFileFromFileDomain(assetImg);
        if(file.exists()) file.delete();
    }

    //multipartfile을 파일 객체로 변환 후 fileDto로 리턴해주는 메소드
    public static FileDto getFileDtoFromMultiPartFile(MultipartFile multipartFile, String uploadUser) throws IOException {
        File file = getMultipartFileToFile(multipartFile);
        String fileName = file.getName();
        String fileType = getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Long fileSize = multipartFile.getSize();
        return FileDto.builder()
                .orgFileName(fileName)
                .filePath(uploadPath + fileName)
                .fileType(fileType)
                .fileSize(fileSize)
                .uploadUser(uploadUser)
                .build();
    }
}
