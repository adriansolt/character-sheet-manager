package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Character;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Character entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterRepository extends ReactiveCrudRepository<Character, Long>, CharacterRepositoryInternal {
    Flux<Character> findAllBy(Pageable pageable);

    @Query("SELECT * FROM character entity WHERE entity.user_id = :id")
    Flux<Character> findByUser(Long id);

    @Query("SELECT * FROM character entity WHERE entity.user_id IS NULL")
    Flux<Character> findAllWhereUserIsNull();

    @Override
    <S extends Character> Mono<S> save(S entity);

    @Override
    Flux<Character> findAll();

    @Override
    Mono<Character> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CharacterRepositoryInternal {
    <S extends Character> Mono<S> save(S entity);

    Flux<Character> findAllBy(Pageable pageable);

    Flux<Character> findAll();

    Mono<Character> findById(Long id);

    Flux<Character> findAllBy(Pageable pageable, Criteria criteria);
}
