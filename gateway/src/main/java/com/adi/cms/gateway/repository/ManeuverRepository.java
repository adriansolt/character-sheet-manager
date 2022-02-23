package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Maneuver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Maneuver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManeuverRepository extends ReactiveCrudRepository<Maneuver, Long>, ManeuverRepositoryInternal {
    Flux<Maneuver> findAllBy(Pageable pageable);

    @Override
    <S extends Maneuver> Mono<S> save(S entity);

    @Override
    Flux<Maneuver> findAll();

    @Override
    Mono<Maneuver> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ManeuverRepositoryInternal {
    <S extends Maneuver> Mono<S> save(S entity);

    Flux<Maneuver> findAllBy(Pageable pageable);

    Flux<Maneuver> findAll();

    Mono<Maneuver> findById(Long id);

    Flux<Maneuver> findAllBy(Pageable pageable, Criteria criteria);
}
