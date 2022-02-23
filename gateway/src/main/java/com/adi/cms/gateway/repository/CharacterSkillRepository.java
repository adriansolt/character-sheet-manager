package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.CharacterSkill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CharacterSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterSkillRepository extends ReactiveCrudRepository<CharacterSkill, Long>, CharacterSkillRepositoryInternal {
    Flux<CharacterSkill> findAllBy(Pageable pageable);

    @Query("SELECT * FROM character_skill entity WHERE entity.character_id = :id")
    Flux<CharacterSkill> findByCharacter(Long id);

    @Query("SELECT * FROM character_skill entity WHERE entity.character_id IS NULL")
    Flux<CharacterSkill> findAllWhereCharacterIsNull();

    @Query("SELECT * FROM character_skill entity WHERE entity.skill_id = :id")
    Flux<CharacterSkill> findBySkill(Long id);

    @Query("SELECT * FROM character_skill entity WHERE entity.skill_id IS NULL")
    Flux<CharacterSkill> findAllWhereSkillIsNull();

    @Override
    <S extends CharacterSkill> Mono<S> save(S entity);

    @Override
    Flux<CharacterSkill> findAll();

    @Override
    Mono<CharacterSkill> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CharacterSkillRepositoryInternal {
    <S extends CharacterSkill> Mono<S> save(S entity);

    Flux<CharacterSkill> findAllBy(Pageable pageable);

    Flux<CharacterSkill> findAll();

    Mono<CharacterSkill> findById(Long id);

    Flux<CharacterSkill> findAllBy(Pageable pageable, Criteria criteria);
}
