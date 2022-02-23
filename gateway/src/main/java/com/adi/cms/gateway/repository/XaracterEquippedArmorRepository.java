package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the XaracterEquippedArmor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterEquippedArmorRepository
    extends ReactiveCrudRepository<XaracterEquippedArmor, Long>, XaracterEquippedArmorRepositoryInternal {
    Flux<XaracterEquippedArmor> findAllBy(Pageable pageable);

    @Query("SELECT * FROM xaracter_equipped_armor entity WHERE entity.armor_piece_id = :id")
    Flux<XaracterEquippedArmor> findByArmorPiece(Long id);

    @Query("SELECT * FROM xaracter_equipped_armor entity WHERE entity.armor_piece_id IS NULL")
    Flux<XaracterEquippedArmor> findAllWhereArmorPieceIsNull();

    @Override
    <S extends XaracterEquippedArmor> Mono<S> save(S entity);

    @Override
    Flux<XaracterEquippedArmor> findAll();

    @Override
    Mono<XaracterEquippedArmor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface XaracterEquippedArmorRepositoryInternal {
    <S extends XaracterEquippedArmor> Mono<S> save(S entity);

    Flux<XaracterEquippedArmor> findAllBy(Pageable pageable);

    Flux<XaracterEquippedArmor> findAll();

    Mono<XaracterEquippedArmor> findById(Long id);

    Flux<XaracterEquippedArmor> findAllBy(Pageable pageable, Criteria criteria);
}
