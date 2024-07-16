package com.kuui.kas.application.file.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

//    @PostMapping("/uploadImg")
//    public ResponseEntity<?> uploadImg(@RequestParam("assetImgFile")MultipartFile[] imgFiles, @RequestParam("assetId") String assetId, Principal principal) throws IOException {
//        log.info("=============================================");
//        log.info("파일 컨트롤러 자산 이미지 들어옴");
//        log.info("=============================================");
//
//        String uploadPath = "D:\\KAS\\images";
//        if (imgFiles.length > 3 ) {
//            return new ResponseEntity<>("You can upload up to 3 images only", HttpStatus.BAD_REQUEST);
//        }
//        List<SaveFile> imageList= new ArrayList<>();
//        for(MultipartFile imgFile : imgFiles) {
//
//            if(imgFile.isEmpty()) {
//                return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
//            }
//
//            String originalFilename = imgFile.getOriginalFilename();
//            String saveName = UUID.randomUUID().toString() + "_" + originalFilename;
//            String fileExt = FileUtil.getExtension(originalFilename);
//
//            SaveFile saveFile = SaveFile.builder()
//                    .orgFileName(originalFilename)
//                    .saveName(saveName)
//                    .filePath(uploadPath)
//                    .fileType(fileExt)
//                    .uploadUser(principal.getName())
//                    .fileSize(imgFile.getSize())
//                    .build();
//
//            imageList.add(saveFile);
//        }
//
//        try{
//            fileService.saveImgFile(imageList, imgFiles);
//        }catch (Exception e) {
//            return new ResponseEntity<>("Error uploading images", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>("Successfully uploaded - " + imgFiles.length + " files ", HttpStatus.OK);
//    }

}
