package com.kuui.kas.application.asset.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.file.repository.SaveFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AssetBulkInsertTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private SaveFileRepository saveFileRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @Transactional
    public void testBulkInsertAssetWithImages() throws Exception{
        //엑셀 파일 경로
        Path excelFilePath = Paths.get("src/test/resources/asset.xlsx");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "asset.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.newInputStream(excelFilePath)
        );

        // 파일 업로드 요청
        mockMvc.perform(multipart("/common/upload").file(file))
                .andExpect(status().isOk());

        //데이터 베이스 검증
        List<Asset> assets = assetRepository.findAll(1, 10);

    }
}
