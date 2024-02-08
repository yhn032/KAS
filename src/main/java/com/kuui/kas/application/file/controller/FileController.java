package com.kuui.kas.application.file.controller;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    @Value("${com.kuui.kas.path.assetImg}")
    private static String uploadPath;

    private final FileService fileService;
    private final AssetService assetService;

    @PostMapping("/uploadImg")
    @ResponseBody
    public String uploadImg(@RequestParam("assetImgFile")MultipartFile imgFile, @RequestParam("assetId") String assetId, Principal principal) throws IOException {
        String originalFilename = imgFile.getOriginalFilename();
        String saveName = UUID.randomUUID().toString() + "_" + originalFilename;
        String fileExt = FileUtil.getExtension(originalFilename);

        System.out.println("originalFilename = " + originalFilename);
        Asset byId = assetService.findById(assetId);
        String uploadPath = "D:\\KAS\\images";

        SaveFile saveFile = SaveFile.builder()
                .orgFileName(originalFilename)
                .saveName(saveName)
                .asset(byId)
                .filePath(uploadPath)
                .fileType(fileExt)
                .uploadUser(principal.getName())
                .fileSize(imgFile.getSize())
                .build();

        fileService.saveFile(FileDto.from(saveFile));

        // 업로드된 파일 경로에 저장하기
        imgFile.transferTo(new File(uploadPath, saveName));

        if (!originalFilename.equals("")) return "success";
        else return "fail";
    }

}
