package com.kuui.kas.application.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    @PostMapping("/uploadImg")
    @ResponseBody
    public String uploadImg(@RequestParam("assetImgFile")MultipartFile imgFile) throws IOException {
        String originalFilename = imgFile.getOriginalFilename();
        System.out.println("originalFilename = " + originalFilename);

        if (!originalFilename.equals("")) return "success";
        else return "fail";
    }

}
