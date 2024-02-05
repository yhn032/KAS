package com.kuui.kas.application.asset.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.common.exception.DuplicateNameAddException;
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

    //Controller 레벨에 익셉션 위임
    public void duplicateAssetName(String name) throws DuplicateNameAddException {
        if(!assetRepository.duplicateAssetName(name).equals("")) {
            //아직 존재하지 않음 신규 추가 가능
            throw new DuplicateNameAddException("이미 존재 하는 상품명 입니다. 상품 수량을 수정해 주세요.");
        }
    }
}
