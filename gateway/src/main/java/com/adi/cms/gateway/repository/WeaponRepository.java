package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Weapon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Weapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponRepository extends ReactiveCrudRepository<Weapon, Long>, WeaponRepositoryInternal {
    Flux<Weapon> findAllBy(Pageable pageable);

    @Query("SELECT * FROM weapon entity WHERE entity.character_id = :id")
    Flux<Weapon> findByCharacter(Long id);

    @Query("SELECT * FROM weapon entity WHERE entity.character_id IS NULL")
    Flux<Weapon> findAllWhereCharacterIsNull();

    @Override
    <S extends Weapon> Mono<S> save(S entity);

    @Override
    Flux<Weapon> findAll();

    @Override
    Mono<Weapon> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WeaponRepositoryInternal {
    <S extends Weapon> Mono<S> save(S entity);

    Flux<Weapon> findAllBy(Pageable pageable);

    Flux<Weapon> findAll();

    Mono<Weapon> findById(Long id);

    Flux<Weapon> findAllBy(Pageable pageable, Criteria criteria);
}
