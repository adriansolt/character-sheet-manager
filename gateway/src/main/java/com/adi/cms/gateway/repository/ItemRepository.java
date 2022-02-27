package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Long>, ItemRepositoryInternal {
    Flux<Item> findAllBy(Pageable pageable);

    @Query("SELECT * FROM item entity WHERE entity.campaign_id = :id")
    Flux<Item> findByCampaign(Long id);

    @Query("SELECT * FROM item entity WHERE entity.campaign_id IS NULL")
    Flux<Item> findAllWhereCampaignIsNull();

    @Query("SELECT * FROM item entity WHERE entity.character_id = :id")
    Flux<Item> findByCharacter(Long id);

    @Query("SELECT * FROM item entity WHERE entity.character_id IS NULL")
    Flux<Item> findAllWhereCharacterIsNull();

    @Override
    <S extends Item> Mono<S> save(S entity);

    @Override
    Flux<Item> findAll();

    @Override
    Mono<Item> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ItemRepositoryInternal {
    <S extends Item> Mono<S> save(S entity);

    Flux<Item> findAllBy(Pageable pageable);

    Flux<Item> findAll();

    Mono<Item> findById(Long id);

    Flux<Item> findAllBy(Pageable pageable, Criteria criteria);
}
