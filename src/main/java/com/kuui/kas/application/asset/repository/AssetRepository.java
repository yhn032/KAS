package com.kuui.kas.application.asset.repository;

import com.kuui.kas.application.asset.domain.Asset;
import com.kuui.kas.application.asset.domain.QAsset;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.kuui.kas.application.asset.domain.QAsset.asset;

@Repository
@RequiredArgsConstructor
public class AssetRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private static final Logger logger = LoggerFactory.getLogger(AssetRepository.class);

    public List<Asset> allAssetList(){
        return queryFactory
                .selectFrom(asset)
                .orderBy(asset.assetRegDate.desc())
                .fetch();
    }

    public void save(Asset asset) {
        logger.info("Ready to Save Asset");
        em.persist(asset);
        logger.info("Success to Save Asset");
    }

    //가장 마지막 물품 번호 구하기
    //이슈 발생 -> 결과값이 2개 이상 -> 쿼리 자체에서 limit 걸었음
    public String LastAssetNo(){
        Asset asset = queryFactory
                .selectFrom(QAsset.asset)
                .orderBy(QAsset.asset.assetRegDate.desc())
                .limit(1)
                .fetchOne();

        if(asset == null) return "kuui-0";
        else return asset.getAssetNo();
    }
}
