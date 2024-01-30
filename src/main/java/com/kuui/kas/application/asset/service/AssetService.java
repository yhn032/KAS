package com.kuui.kas.application.asset.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> allAssetList(){
        return assetRepository.allAssetList();
    }

    @Transactional
    public void saveAsset(Asset asset) {assetRepository.save(asset);}

    public String LastAssetNo(){return assetRepository.LastAssetNo();}
}
