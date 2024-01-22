package com.kuui.kas.application.teacher.controller;

import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/duplicateId")
    @ResponseBody
    public String duplicateId(String id){

        String result = "";
        try{
            teacherService.validDuplicateID(id);
            result = "success";
        }catch (IllegalStateException e){
            e.printStackTrace();
            result = "error";
        }

        return result;
    }

}
