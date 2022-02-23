package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends ReactiveCrudRepository<Skill, Long>, SkillRepositoryInternal {
    Flux<Skill> findAllBy(Pageable pageable);

    @Override
    <S extends Skill> Mono<S> save(S entity);

    @Override
    Flux<Skill> findAll();

    @Override
    Mono<Skill> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SkillRepositoryInternal {
    <S extends Skill> Mono<S> save(S entity);

    Flux<Skill> findAllBy(Pageable pageable);

    Flux<Skill> findAll();

    Mono<Skill> findById(Long id);

    Flux<Skill> findAllBy(Pageable pageable, Criteria criteria);
}
