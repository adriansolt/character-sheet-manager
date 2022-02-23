package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.CampaignUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CampaignUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignUserRepository extends ReactiveCrudRepository<CampaignUser, Long>, CampaignUserRepositoryInternal {
    Flux<CampaignUser> findAllBy(Pageable pageable);

    @Query("SELECT * FROM campaign_user entity WHERE entity.campaign_id_id = :id")
    Flux<CampaignUser> findByCampaignId(Long id);

    @Query("SELECT * FROM campaign_user entity WHERE entity.campaign_id_id IS NULL")
    Flux<CampaignUser> findAllWhereCampaignIdIsNull();

    @Query("SELECT * FROM campaign_user entity WHERE entity.user_id = :id")
    Flux<CampaignUser> findByUser(Long id);

    @Query("SELECT * FROM campaign_user entity WHERE entity.user_id IS NULL")
    Flux<CampaignUser> findAllWhereUserIsNull();

    @Override
    <S extends CampaignUser> Mono<S> save(S entity);

    @Override
    Flux<CampaignUser> findAll();

    @Override
    Mono<CampaignUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CampaignUserRepositoryInternal {
    <S extends CampaignUser> Mono<S> save(S entity);

    Flux<CampaignUser> findAllBy(Pageable pageable);

    Flux<CampaignUser> findAll();

    Mono<CampaignUser> findById(Long id);

    Flux<CampaignUser> findAllBy(Pageable pageable, Criteria criteria);
}
