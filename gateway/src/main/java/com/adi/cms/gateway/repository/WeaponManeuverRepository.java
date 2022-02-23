package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.WeaponManeuver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the WeaponManeuver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponManeuverRepository extends ReactiveCrudRepository<WeaponManeuver, Long>, WeaponManeuverRepositoryInternal {
    Flux<WeaponManeuver> findAllBy(Pageable pageable);

    @Query("SELECT * FROM weapon_maneuver entity WHERE entity.weapon_id_id = :id")
    Flux<WeaponManeuver> findByWeaponId(Long id);

    @Query("SELECT * FROM weapon_maneuver entity WHERE entity.weapon_id_id IS NULL")
    Flux<WeaponManeuver> findAllWhereWeaponIdIsNull();

    @Query("SELECT * FROM weapon_maneuver entity WHERE entity.maneuver_id_id = :id")
    Flux<WeaponManeuver> findByManeuverId(Long id);

    @Query("SELECT * FROM weapon_maneuver entity WHERE entity.maneuver_id_id IS NULL")
    Flux<WeaponManeuver> findAllWhereManeuverIdIsNull();

    @Override
    <S extends WeaponManeuver> Mono<S> save(S entity);

    @Override
    Flux<WeaponManeuver> findAll();

    @Override
    Mono<WeaponManeuver> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WeaponManeuverRepositoryInternal {
    <S extends WeaponManeuver> Mono<S> save(S entity);

    Flux<WeaponManeuver> findAllBy(Pageable pageable);

    Flux<WeaponManeuver> findAll();

    Mono<WeaponManeuver> findById(Long id);

    Flux<WeaponManeuver> findAllBy(Pageable pageable, Criteria criteria);
}
