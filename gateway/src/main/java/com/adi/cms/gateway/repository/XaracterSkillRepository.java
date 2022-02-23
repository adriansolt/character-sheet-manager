package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.XaracterSkill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the XaracterSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterSkillRepository extends ReactiveCrudRepository<XaracterSkill, Long>, XaracterSkillRepositoryInternal {
    Flux<XaracterSkill> findAllBy(Pageable pageable);

    @Query("SELECT * FROM xaracter_skill entity WHERE entity.xaracter_id_id = :id")
    Flux<XaracterSkill> findByXaracterId(Long id);

    @Query("SELECT * FROM xaracter_skill entity WHERE entity.xaracter_id_id IS NULL")
    Flux<XaracterSkill> findAllWhereXaracterIdIsNull();

    @Query("SELECT * FROM xaracter_skill entity WHERE entity.skill_id_id = :id")
    Flux<XaracterSkill> findBySkillId(Long id);

    @Query("SELECT * FROM xaracter_skill entity WHERE entity.skill_id_id IS NULL")
    Flux<XaracterSkill> findAllWhereSkillIdIsNull();

    @Override
    <S extends XaracterSkill> Mono<S> save(S entity);

    @Override
    Flux<XaracterSkill> findAll();

    @Override
    Mono<XaracterSkill> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface XaracterSkillRepositoryInternal {
    <S extends XaracterSkill> Mono<S> save(S entity);

    Flux<XaracterSkill> findAllBy(Pageable pageable);

    Flux<XaracterSkill> findAll();

    Mono<XaracterSkill> findById(Long id);

    Flux<XaracterSkill> findAllBy(Pageable pageable, Criteria criteria);
}
