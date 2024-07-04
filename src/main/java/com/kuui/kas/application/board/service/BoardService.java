package com.kuui.kas.application.board.service;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.asset.service.AssetService;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.board.exception.NoRemainAssetException;
import com.kuui.kas.application.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final AssetRepository assetRepository;
    public Board findById(Long id) {
        return boardRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Board addShareBoard(Board board) throws NoRemainAssetException{
        //게시판에 등록하기 전에 반출 수량을 마이너스 해준다. 0보다 작아지면 exception
        Asset asset = assetRepository.findById(board.getBoardAsset().getAssetId());
        Long assetRemainCnt = asset.getAssetRemainCnt();
        if(assetRemainCnt - board.getBoardShareCount() <= 0) {
            throw new NoRemainAssetException("남은 수량보다 대여하는 수량이 더 많습니다. 재고를 확인하고 반출 수량을 다시 입력하세요.");
        }
//        Asset mergeAsset = assetRepository.saveAsset(new Asset(
//                asset.getAssetId()
//                , asset.getAssetNo()
//                , asset.getAssetName()
//                , asset.getAssetTotalCnt()
//                , assetRemainCnt - board.getBoardShareCount()
//                , asset.getAssetCtg()
//                , asset.getAssetPos()
//                , asset.getRegTeacherName()
//                , asset.getUpdTeacherName()));


        return boardRepository.addBoard(board);
    }
}
