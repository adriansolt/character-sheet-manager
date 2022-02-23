package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Xaracter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Xaracter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterRepository extends ReactiveCrudRepository<Xaracter, Long>, XaracterRepositoryInternal {
    Flux<Xaracter> findAllBy(Pageable pageable);

    @Query("SELECT * FROM xaracter entity WHERE entity.user_id = :id")
    Flux<Xaracter> findByUser(Long id);

    @Query("SELECT * FROM xaracter entity WHERE entity.user_id IS NULL")
    Flux<Xaracter> findAllWhereUserIsNull();

    @Override
    <S extends Xaracter> Mono<S> save(S entity);

    @Override
    Flux<Xaracter> findAll();

    @Override
    Mono<Xaracter> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface XaracterRepositoryInternal {
    <S extends Xaracter> Mono<S> save(S entity);

    Flux<Xaracter> findAllBy(Pageable pageable);

    Flux<Xaracter> findAll();

    Mono<Xaracter> findById(Long id);

    Flux<Xaracter> findAllBy(Pageable pageable, Criteria criteria);
}
