package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.ArmorPiece;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the ArmorPiece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArmorPieceRepository extends ReactiveCrudRepository<ArmorPiece, Long>, ArmorPieceRepositoryInternal {
    Flux<ArmorPiece> findAllBy(Pageable pageable);

    @Query("SELECT * FROM armor_piece entity WHERE entity.campaign_id = :id")
    Flux<ArmorPiece> findByCampaign(Long id);

    @Query("SELECT * FROM armor_piece entity WHERE entity.campaign_id IS NULL")
    Flux<ArmorPiece> findAllWhereCampaignIsNull();

    @Query("SELECT * FROM armor_piece entity WHERE entity.character_id = :id")
    Flux<ArmorPiece> findByCharacter(Long id);

    @Query("SELECT * FROM armor_piece entity WHERE entity.character_id IS NULL")
    Flux<ArmorPiece> findAllWhereCharacterIsNull();

    @Override
    <S extends ArmorPiece> Mono<S> save(S entity);

    @Override
    Flux<ArmorPiece> findAll();

    @Override
    Mono<ArmorPiece> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ArmorPieceRepositoryInternal {
    <S extends ArmorPiece> Mono<S> save(S entity);

    Flux<ArmorPiece> findAllBy(Pageable pageable);

    Flux<ArmorPiece> findAll();

    Mono<ArmorPiece> findById(Long id);

    Flux<ArmorPiece> findAllBy(Pageable pageable, Criteria criteria);
}
