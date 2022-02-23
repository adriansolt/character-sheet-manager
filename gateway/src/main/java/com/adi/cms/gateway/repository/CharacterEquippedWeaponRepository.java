package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CharacterEquippedWeapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterEquippedWeaponRepository
    extends ReactiveCrudRepository<CharacterEquippedWeapon, Long>, CharacterEquippedWeaponRepositoryInternal {
    Flux<CharacterEquippedWeapon> findAllBy(Pageable pageable);

    @Query("SELECT * FROM character_equipped_weapon entity WHERE entity.weapon_id = :id")
    Flux<CharacterEquippedWeapon> findByWeapon(Long id);

    @Query("SELECT * FROM character_equipped_weapon entity WHERE entity.weapon_id IS NULL")
    Flux<CharacterEquippedWeapon> findAllWhereWeaponIsNull();

    @Override
    <S extends CharacterEquippedWeapon> Mono<S> save(S entity);

    @Override
    Flux<CharacterEquippedWeapon> findAll();

    @Override
    Mono<CharacterEquippedWeapon> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CharacterEquippedWeaponRepositoryInternal {
    <S extends CharacterEquippedWeapon> Mono<S> save(S entity);

    Flux<CharacterEquippedWeapon> findAllBy(Pageable pageable);

    Flux<CharacterEquippedWeapon> findAll();

    Mono<CharacterEquippedWeapon> findById(Long id);

    Flux<CharacterEquippedWeapon> findAllBy(Pageable pageable, Criteria criteria);
}
