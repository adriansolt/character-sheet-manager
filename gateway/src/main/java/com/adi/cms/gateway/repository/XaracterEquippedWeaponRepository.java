package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.XaracterEquippedWeapon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the XaracterEquippedWeapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterEquippedWeaponRepository
    extends ReactiveCrudRepository<XaracterEquippedWeapon, Long>, XaracterEquippedWeaponRepositoryInternal {
    Flux<XaracterEquippedWeapon> findAllBy(Pageable pageable);

    @Query("SELECT * FROM xaracter_equipped_weapon entity WHERE entity.weapon_id_id = :id")
    Flux<XaracterEquippedWeapon> findByWeaponId(Long id);

    @Query("SELECT * FROM xaracter_equipped_weapon entity WHERE entity.weapon_id_id IS NULL")
    Flux<XaracterEquippedWeapon> findAllWhereWeaponIdIsNull();

    @Override
    <S extends XaracterEquippedWeapon> Mono<S> save(S entity);

    @Override
    Flux<XaracterEquippedWeapon> findAll();

    @Override
    Mono<XaracterEquippedWeapon> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface XaracterEquippedWeaponRepositoryInternal {
    <S extends XaracterEquippedWeapon> Mono<S> save(S entity);

    Flux<XaracterEquippedWeapon> findAllBy(Pageable pageable);

    Flux<XaracterEquippedWeapon> findAll();

    Mono<XaracterEquippedWeapon> findById(Long id);

    Flux<XaracterEquippedWeapon> findAllBy(Pageable pageable, Criteria criteria);
}
