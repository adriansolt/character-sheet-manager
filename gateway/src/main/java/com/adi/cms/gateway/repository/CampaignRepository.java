package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Campaign;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Campaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignRepository extends ReactiveCrudRepository<Campaign, Long>, CampaignRepositoryInternal {
    Flux<Campaign> findAllBy(Pageable pageable);

    @Override
    <S extends Campaign> Mono<S> save(S entity);

    @Override
    Flux<Campaign> findAll();

    @Override
    Mono<Campaign> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CampaignRepositoryInternal {
    <S extends Campaign> Mono<S> save(S entity);

    Flux<Campaign> findAllBy(Pageable pageable);

    Flux<Campaign> findAll();

    Mono<Campaign> findById(Long id);

    Flux<Campaign> findAllBy(Pageable pageable, Criteria criteria);
}
