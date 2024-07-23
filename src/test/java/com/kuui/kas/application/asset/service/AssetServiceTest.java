package com.kuui.kas.application.asset.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.file.domain.SaveFile;
import com.kuui.kas.application.file.service.FileService;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.kuui.kas.application.teacher.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)         //Mockito를 사용한 단위 테스트를 지원하는 확장 클래스. 이를 통해 Mock객체 사용가능
class AssetServiceTest {
    @Mock       //Mockito의 어노테이션으로 목 객체 생성
    private AssetRepository assetRepository;

    @Mock
    private FileService fileService;
    @Mock
    private TeacherService teacherService;

    @InjectMocks//Mockito의 어노테이션으로 객체 생성후 목 객체 주입
    private AssetService assetService;

    private Asset asset;
    private SaveFile saveFile;
    private Board board;
    private Teacher teacher;

    @BeforeEach
    public void setUp(){
        saveFile = SaveFile.builder()
                .orgFileName("test")
                .saveName("test1")
                .filePath("D:\\KasImg\\asset")
                .fileType("png")
                .uploadUser("김병국")
                .fileSize(1240L)
                .build();

        asset = new Asset("id1", "kuui-198", "test", 12L, 12L, "행사", 12, "김병국", "김병국", Arrays.asList(saveFile),"","");

        board = Board.builder()
                .boardAsset(asset)
                .boardTeacher(teacherService.findByTeacherNickName(asset.getRegTeacherName()))
                .boardCarryInName("김병국")
                .boardAssetReturnYn("N")
                .boardShareCount(12L)
                .boardRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();


    }

    @Test
    public void testDeleteAssetSuccess(){
        String assetId = "86478c92-f33d-46df-af12-ecb6776cd625";
        Asset asset = new Asset(assetId, "kuui-12", "자물쇠", 123L, 123L, "", 1, "", "" ,null,"","");

        when(assetRepository.findById(assetId)).thenReturn(asset);

        assetService.deleteAsset(assetId);

        //assetRepository에서 삭제 메서드가 1번 호출되었는지 검증
        verify(assetRepository, times(1)).deleteAsset(assetId);
    }


    @Test
    public void testDeleteAssetNotFound(){
        String assetId = "test";

        when(assetRepository.findById(assetId)).thenReturn(null);

        //메서드 호출시 EntityNotFoundException이 발생하는지 검증한다.
        assertThrows(EntityNotFoundException.class, () -> {
            assetService.deleteAsset(assetId);
        });

        //해당 메서드가 호출되지 않았는지 검증 -> 호출 전에 exception이 터져서 호출이 되면 안 되는게 맞음
        verify(assetRepository, never()).deleteAsset(assetId);
    }

    @Test
    public void testDeleteAssetBuisnessLogic(){
        String assetId = "40e395b1-0cdf-45ed-971f-4c6a0e62a327";
        Asset asset = new Asset(assetId, "", "", 0L, 0L, "", 1, "", "" ,null,"","");

        when(assetRepository.findById(assetId)).thenReturn(asset);

        assertThrows(DataIntegrityViolationException.class, () -> {
           assetService.deleteAsset(assetId);
        });

        verify(assetRepository, never()).deleteAsset(assetId);
    }

    @Test
    public void testDeleteAsset() {
        when(assetRepository.findById("id1")).thenReturn(asset);

        //엔티티 삭제 호출
        assetService.deleteAsset("id1");

        verify(fileService, times(1)).deleteFileForAsset(asset.getAssetImgs());
        verify(assetRepository, times(1)).deleteAsset(asset.getAssetId());

        // 파일이 실제로 삭제되었는지 확인
        File file = new File(saveFile.getFilePath(), saveFile.getSaveName());
        assert !file.exists() : "파일이 삭제되지 않았습니다.";
    }
}


/**
 * 테스트 코드에서 삭제된 내용이 실제로 삭제가 반영되지 않는 이유
 *
 * 목 객체 사용: 테스트는 ProductRepository의 목 객체를 사용합니다. 목 객체는 실제 데이터베이스와 상호 작용하지 않으며, 설정된 동작을 시뮬레이션할 뿐입니다.
 * 트랜잭션 관리: 테스트 환경에서는 보통 트랜잭션이 커밋되지 않습니다. 테스트 후 트랜잭션이 롤백되기 때문에, 테스트 중 수행된 모든 데이터 변경 사항은 무시됩니다.
 * 테스트 격리: 단위 테스트는 실제 데이터베이스에 영향을 주지 않도록 설계됩니다. 이는 테스트의 신뢰성과 재현성을 높이기 위한 일반적인 관행입니다.
 */