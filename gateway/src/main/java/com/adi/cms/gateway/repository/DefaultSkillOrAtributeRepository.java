package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the DefaultSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefaultSkillOrAtributeRepository
    extends ReactiveCrudRepository<DefaultSkillOrAtribute, Long>, DefaultSkillOrAtributeRepositoryInternal {
    Flux<DefaultSkillOrAtribute> findAllBy(Pageable pageable);

    @Query("SELECT * FROM default_skill_or_atribute entity WHERE entity.skill_id_id = :id")
    Flux<DefaultSkillOrAtribute> findBySkillId(Long id);

    @Query("SELECT * FROM default_skill_or_atribute entity WHERE entity.skill_id_id IS NULL")
    Flux<DefaultSkillOrAtribute> findAllWhereSkillIdIsNull();

    @Override
    <S extends DefaultSkillOrAtribute> Mono<S> save(S entity);

    @Override
    Flux<DefaultSkillOrAtribute> findAll();

    @Override
    Mono<DefaultSkillOrAtribute> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DefaultSkillOrAtributeRepositoryInternal {
    <S extends DefaultSkillOrAtribute> Mono<S> save(S entity);

    Flux<DefaultSkillOrAtribute> findAllBy(Pageable pageable);

    Flux<DefaultSkillOrAtribute> findAll();

    Mono<DefaultSkillOrAtribute> findById(Long id);

    Flux<DefaultSkillOrAtribute> findAllBy(Pageable pageable, Criteria criteria);
}
