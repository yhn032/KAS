package com.kuui.kas.application.teacher.controller;

import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.domain.TeacherRole;
import com.kuui.kas.application.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    @GetMapping("/manage")
    public String manageTeacherList(Model model){
        List<Teacher> allTeachers = teacherService.findAllTeachers();

        model.addAttribute("teachers", allTeachers);
        return "/teacher/allTeacherList";
    }

}