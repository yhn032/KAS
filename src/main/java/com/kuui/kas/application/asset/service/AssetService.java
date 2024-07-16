package com.kuui.kas.application.asset.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.common.exception.DuplicateNameAddException;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetService {
    private final AssetRepository assetRepository;
    private final FileService fileService;

    public List<String> findAllCtg(){return assetRepository.findAllCtg();}

    public List<Asset> findDataByCtg(String ctg){return assetRepository.findDataByCtg(ctg);}

    public List<Asset> findAll(int page, int pageUnit){
        return assetRepository.findAll(page, pageUnit);
    }

    @Transactional
    public Asset saveAsset(Asset asset) {return assetRepository.saveAsset(asset);}

    public String LastAssetNo(){return assetRepository.LastAssetNo();}

    //Controller 레벨에 익셉션 위임
    public void duplicateAssetName(String name) throws DuplicateNameAddException {
        if(!assetRepository.duplicateAssetName(name).equals("")) {
            //아직 존재하지 않음 신규 추가 가능
            throw new DuplicateNameAddException("이미 존재 하는 상품명 입니다. 상품 수량을 수정해 주세요.");
        }
    }

    public Asset findById(String assetId) {return assetRepository.findById(assetId);}


    @Transactional(rollbackFor = Exception.class)   //언제든 익셉션이 터지면 롤백
    //재고 및 이미지 추가하기
    public void addAssetWithImage(Asset asset, MultipartFile[] multipartFiles, Principal principal) throws IOException, DuplicateNameAddException {
        //0. 파일 정보 DB에 저장
        List<SaveFile> imageList= new ArrayList<>();
        String uploadPath = "D:\\KAS\\images";
        for(MultipartFile imgFile : multipartFiles) {

            if(imgFile.isEmpty()) {
                throw new FileExistsException("Please select a file to upload");
            }

            String originalFilename = imgFile.getOriginalFilename();
            String saveName = UUID.randomUUID().toString() + "_" + originalFilename;
            String fileExt = FileUtil.getExtension(originalFilename);

            SaveFile saveFile = SaveFile.builder()
                    .orgFileName(originalFilename)
                    .saveName(saveName)
                    .filePath(uploadPath)
                    .fileType(fileExt)
                    .uploadUser(principal.getName())
                    .fileSize(imgFile.getSize())
                    .build();

            imageList.add(saveFile);
        }

        try{
            fileService.saveImgFile(imageList, multipartFiles);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error uploading images");
        }

        //1. 재고를 등록하기 전에 동일한 이름이 존재한다면 수량을 수정하도록 예외 호출
        duplicateAssetName(asset.getAssetName());
        //--예외가 터지지 않으면 신규 등록 가능한 재고이다.--

        //2. 마지막으로 저장된 재고 번호 조회하기
        String lastAssetNo = LastAssetNo();
        int num = Integer.parseInt(lastAssetNo.substring(lastAssetNo.indexOf("-") + 1, lastAssetNo.length()));
        Asset newAsset = new Asset(UUID.randomUUID().toString(), "kuui-" + (++num), asset.getAssetName(), asset.getAssetTotalCnt(), asset.getAssetRemainCnt(), asset.getAssetCtg(),asset.getAssetPos(), asset.getRegTeacherName(), asset.getRegTeacherName(), imageList);

        //3. 물품 저장하기
        Asset saveAsset = saveAsset(newAsset);
    }


    public List<Asset> searchAsset(String searchTerm, int page, int pageUnit) {

        return assetRepository.searchAsset(searchTerm, page, pageUnit);
    }
}
