package com.kuui.kas.application.asset.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.board.exception.NoRemainAssetException;
import com.kuui.kas.application.common.exception.DuplicateNameAddException;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.dto.FileDto;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
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
        String uploadPath = "D:\\KasImg\\asset\\";
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

    @Transactional(rollbackFor = Exception.class)   //언제든 익셉션이 터지면 롤백
    //재고 및 이미지 추가하기
    public void modifyAssetWithImage(Asset asset, MultipartFile[] multipartFiles, Principal principal) throws IOException, DuplicateNameAddException, NoRemainAssetException {
        List<SaveFile> imageList= new ArrayList<>();
        String uploadPath = "D:\\KasImg\\asset\\";
        //변경 감지
        String assetId = asset.getAssetId();

        Asset prevAsset = assetRepository.findById(assetId);
        //현재 잔여 수량 + (변경된 전체 수량 - 변경전 전체 수량)
        Long remainCnt = prevAsset.getAssetRemainCnt() + (asset.getAssetTotalCnt() - prevAsset.getAssetTotalCnt());
        if(remainCnt < 0) {
            throw new NoRemainAssetException("잔여 수량이 0개 입니다. 반출된 내역을 반납 처리 하지 않았다면, 반납후 다시 수정하세요.");
        }

        //0. 파일 정보 DB에 저장
        if(!multipartFiles[0].isEmpty()) { //파일을 수정한 경우에만 수정하기.
            //기존 파일 삭제
            for(SaveFile file : prevAsset.getAssetImgs() ){
                String imgPath = uploadPath + "\\" + file.getSaveName();
                Path imagePath = Paths.get(imgPath);

                try {
                    Files.deleteIfExists(imagePath);
                    log.info("이미지 파일이 파일 시스템에서 성공적으로 삭제되었습니다.");
                } catch (IOException e) {
                    log.info("이미지 파일을 삭제하는 중에 오류가 발생했습니다.");
                    e.printStackTrace();
                }
            }
            //신규 파일 등록
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
        }

        Asset newAsset = new Asset(prevAsset.getAssetId(), prevAsset.getAssetNo(), asset.getAssetName(), asset.getAssetTotalCnt(), remainCnt, asset.getAssetCtg(), asset.getAssetPos(), asset.getRegTeacherName(), asset.getUpdTeacherName(), multipartFiles.length==0 ? prevAsset.getAssetImgs() : imageList);
        //3. 물품 저장하기
        Asset saveAsset = saveAsset(newAsset);
    }


    public List<Asset> searchAsset(String searchTerm, int page, int pageUnit) {

        return assetRepository.searchAsset(searchTerm, page, pageUnit);
    }

    /**
     * 자산 삭제
     */
    @Transactional(rollbackFor = Exception.class)
    public Long deleteAsset(String assetId){

        Asset asset = assetRepository.findById(assetId);
        Long executeCnt = 0L;
        if(asset == null) {
            throw new EntityNotFoundException("Entity Not Found");
        }

        //삭제 가능 여부 파악하기 -> 게시판에 등록되어 있는 물건 삭제 불가.
        if (!asset.isDeletable()) {
            throw new DataIntegrityViolationException("Asset cannot be deleted in its current state as it is referenced by Board Entity");
        }

        try{
            //파일 시스템에서 삭제하기
            fileService.deleteFileForAsset(asset.getAssetImgs());
//            for(SaveFile file : asset.getAssetImgs()) {
//                String imgPath = file.getFilePath() + "\\" + file.getSaveName();
//                Path imagePath = Paths.get(imgPath);
//
//                try {
//                    Files.deleteIfExists(imagePath);
//                    log.info("이미지 파일이 파일 시스템에서 성공적으로 삭제되었습니다.");
//                } catch (IOException e) {
//                    log.info("이미지 파일을 삭제하는 중에 오류가 발생했습니다.");
//                    e.printStackTrace();
//                }
//            }

            //실제 삭제
            executeCnt = assetRepository.deleteAsset(assetId);
        }catch(DataIntegrityViolationException e) {
            //외래 키 제약 조건 위반 처리
            throw new DataIntegrityViolationException("Cannot delete asset as it is referenced by other entities ");
        }

        return executeCnt;
    }
}
