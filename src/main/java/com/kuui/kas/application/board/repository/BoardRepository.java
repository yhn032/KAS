package com.kuui.kas.application.board.repository;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.repository.AssetRepository;
import com.kuui.kas.application.board.domain.Board;
import com.kuui.kas.application.board.domain.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.kuui.kas.application.board.domain.QBoard.board;

@Repository
@RequiredArgsConstructor

public class BoardRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private static final Logger logger = LoggerFactory.getLogger(BoardRepository.class);

    public Board addBoard(Board board){
        logger.info("Ready to Save Board");
        em.persist(board);
        return board;
    }

    public Board findById(Long id) {
        return queryFactory
                .selectFrom(board)
                .where(board.boardId.eq(id))
                .fetchOne();
    }

    public List<Board> findAllShareList(int page, int pageUnit) {
        long offSet = (page-1)*pageUnit;        //offSet값부터 limitsize개수만큼 결과를 가져온다.
        return queryFactory
                .selectFrom(board)
                .orderBy(board.boardRegDate.desc())
                .limit(pageUnit)
                .offset(offSet)
                .fetch();
    }
}
