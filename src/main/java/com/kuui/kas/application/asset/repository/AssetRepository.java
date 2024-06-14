package com.kuui.kas.application.asset.repository;

import com.kuui.kas.application.asset.domain.Asset;
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
public class AssetRepository{


    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private static final Logger logger = LoggerFactory.getLogger(AssetRepository.class);


    public Asset saveAsset(Asset asset) {
        logger.info("Ready to Save Asset");
//        if(entityInformation.isNew(asset)) {
//            em.persist(asset);
//            return asset;
//        }else {
//            return em.merge(asset);
//        }

        Optional<Asset> tmp = Optional.ofNullable(findById(asset.getAssetId()));
        if(tmp.isPresent()){
            return em.merge(asset);
        }else {
            em.persist(asset);
            return asset;
        }

    }

    //가장 마지막 물품 번호 구하기
    //이슈 발생 -> 결과값이 2개 이상 -> 쿼리 자체에서 limit 걸었음
    public String LastAssetNo(){
        Asset newAsset = queryFactory
                .selectFrom(asset)
                .orderBy(asset.assetRegDate.desc())
                .limit(1)
                .fetchOne();

        if(newAsset == null) return "kuui-0";
        else return newAsset.getAssetNo();
    }

    //동일한 재고명이 존재하는지 파악하기
    public String duplicateAssetName(String name){
        Asset asset1 = queryFactory
                .selectFrom(asset)
                .where(asset.assetName.eq(name))
                .fetchOne();

        if(asset1 == null) return "";
        else return  asset1.getAssetName();
    }

    public String getAssetIdFindByAssetNo(String assetNo){
        Asset asset1 = queryFactory
                .selectFrom(asset)
                .where(asset.assetNo.eq(assetNo))
                .fetchOne();

        return asset1.getAssetId();
    }

    public List<Asset> findAll() {
        return queryFactory
                .selectFrom(asset)
                .orderBy(asset.assetUpdDate.desc())
                .fetch();
    }

    public Asset findById(String assetId) {
        return queryFactory
                .selectFrom(asset)
                .where(asset.assetId.eq(assetId))
                .fetchOne();
    }

    public List<Asset> searchAsset(String searchTerm) {
        return queryFactory
                .selectFrom(asset)
                .where(asset.assetName.contains(searchTerm))
                .orderBy(asset.assetUpdDate.desc())
                .fetch();
    }
}
