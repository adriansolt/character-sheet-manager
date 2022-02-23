package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.XaracterAttribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the XaracterAttribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterAttributeRepository extends ReactiveCrudRepository<XaracterAttribute, Long>, XaracterAttributeRepositoryInternal {
    Flux<XaracterAttribute> findAllBy(Pageable pageable);

    @Query("SELECT * FROM xaracter_attribute entity WHERE entity.xaracter_id_id = :id")
    Flux<XaracterAttribute> findByXaracterId(Long id);

    @Query("SELECT * FROM xaracter_attribute entity WHERE entity.xaracter_id_id IS NULL")
    Flux<XaracterAttribute> findAllWhereXaracterIdIsNull();

    @Override
    <S extends XaracterAttribute> Mono<S> save(S entity);

    @Override
    Flux<XaracterAttribute> findAll();

    @Override
    Mono<XaracterAttribute> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface XaracterAttributeRepositoryInternal {
    <S extends XaracterAttribute> Mono<S> save(S entity);

    Flux<XaracterAttribute> findAllBy(Pageable pageable);

    Flux<XaracterAttribute> findAll();

    Mono<XaracterAttribute> findById(Long id);

    Flux<XaracterAttribute> findAllBy(Pageable pageable, Criteria criteria);
}
