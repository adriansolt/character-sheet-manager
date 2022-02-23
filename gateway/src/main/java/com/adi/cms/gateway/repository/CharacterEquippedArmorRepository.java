package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.CharacterEquippedArmor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CharacterEquippedArmor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterEquippedArmorRepository
    extends ReactiveCrudRepository<CharacterEquippedArmor, Long>, CharacterEquippedArmorRepositoryInternal {
    Flux<CharacterEquippedArmor> findAllBy(Pageable pageable);

    @Query("SELECT * FROM character_equipped_armor entity WHERE entity.armor_piece_id = :id")
    Flux<CharacterEquippedArmor> findByArmorPiece(Long id);

    @Query("SELECT * FROM character_equipped_armor entity WHERE entity.armor_piece_id IS NULL")
    Flux<CharacterEquippedArmor> findAllWhereArmorPieceIsNull();

    @Override
    <S extends CharacterEquippedArmor> Mono<S> save(S entity);

    @Override
    Flux<CharacterEquippedArmor> findAll();

    @Override
    Mono<CharacterEquippedArmor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CharacterEquippedArmorRepositoryInternal {
    <S extends CharacterEquippedArmor> Mono<S> save(S entity);

    Flux<CharacterEquippedArmor> findAllBy(Pageable pageable);

    Flux<CharacterEquippedArmor> findAll();

    Mono<CharacterEquippedArmor> findById(Long id);

    Flux<CharacterEquippedArmor> findAllBy(Pageable pageable, Criteria criteria);
}
