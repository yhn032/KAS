package com.kuui.kas.application.common.service;

import com.kuui.kas.application.common.repository.CommonRepository;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CommonRepository commonRepository;

}
