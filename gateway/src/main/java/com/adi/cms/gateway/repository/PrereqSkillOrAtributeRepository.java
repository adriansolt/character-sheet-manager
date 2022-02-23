package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.PrereqSkillOrAtribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the PrereqSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrereqSkillOrAtributeRepository
    extends ReactiveCrudRepository<PrereqSkillOrAtribute, Long>, PrereqSkillOrAtributeRepositoryInternal {
    Flux<PrereqSkillOrAtribute> findAllBy(Pageable pageable);

    @Query("SELECT * FROM prereq_skill_or_atribute entity WHERE entity.skill_id = :id")
    Flux<PrereqSkillOrAtribute> findBySkill(Long id);

    @Query("SELECT * FROM prereq_skill_or_atribute entity WHERE entity.skill_id IS NULL")
    Flux<PrereqSkillOrAtribute> findAllWhereSkillIsNull();

    @Override
    <S extends PrereqSkillOrAtribute> Mono<S> save(S entity);

    @Override
    Flux<PrereqSkillOrAtribute> findAll();

    @Override
    Mono<PrereqSkillOrAtribute> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PrereqSkillOrAtributeRepositoryInternal {
    <S extends PrereqSkillOrAtribute> Mono<S> save(S entity);

    Flux<PrereqSkillOrAtribute> findAllBy(Pageable pageable);

    Flux<PrereqSkillOrAtribute> findAll();

    Mono<PrereqSkillOrAtribute> findById(Long id);

    Flux<PrereqSkillOrAtribute> findAllBy(Pageable pageable, Criteria criteria);
}
