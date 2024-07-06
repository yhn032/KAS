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
        if(asset == null) {
            throw new NullPointerException("Asset Not Found");
        }

        //변경 감지를 위한 엔티티 수정
        asset.share(board.getBoardShareCount());

        return boardRepository.addBoard(board);
    }
}
