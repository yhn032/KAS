package com.kuui.kas.application.asset.controller;

import com.kuui.kas.application.asset.repository.AssetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AssetControllerTest {

    @Autowired
    AssetRepository assetRepository;

    @Test
    @DisplayName("물품 추가 테스트")
    public void addAsset(){
        String s = UUID.randomUUID().toString();
        System.out.println(s);
        String assetNo = assetRepository.LastAssetNo();

        Assertions.assertEquals("kuui-0", assetNo);
    }
}