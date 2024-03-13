package com.kuui.kas.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMVCConfig implements WebMvcConfigurer {
    private String imgLocalPath = "file:D:/KasImg/asset/";

    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:8080/img/uploads/asset
    //imgLocalPath 실질적인 파일저장위치를 설정해줍니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/uploads/**")
                .addResourceLocations(imgLocalPath);
    }
}
