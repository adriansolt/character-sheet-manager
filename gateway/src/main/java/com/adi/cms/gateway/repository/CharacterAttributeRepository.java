package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.CharacterAttribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CharacterAttribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterAttributeRepository
    extends ReactiveCrudRepository<CharacterAttribute, Long>, CharacterAttributeRepositoryInternal {
    Flux<CharacterAttribute> findAllBy(Pageable pageable);

    @Query("SELECT * FROM character_attribute entity WHERE entity.character_id = :id")
    Flux<CharacterAttribute> findByCharacter(Long id);

    @Query("SELECT * FROM character_attribute entity WHERE entity.character_id IS NULL")
    Flux<CharacterAttribute> findAllWhereCharacterIsNull();

    @Override
    <S extends CharacterAttribute> Mono<S> save(S entity);

    @Override
    Flux<CharacterAttribute> findAll();

    @Override
    Mono<CharacterAttribute> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CharacterAttributeRepositoryInternal {
    <S extends CharacterAttribute> Mono<S> save(S entity);

    Flux<CharacterAttribute> findAllBy(Pageable pageable);

    Flux<CharacterAttribute> findAll();

    Mono<CharacterAttribute> findById(Long id);

    Flux<CharacterAttribute> findAllBy(Pageable pageable, Criteria criteria);
}
