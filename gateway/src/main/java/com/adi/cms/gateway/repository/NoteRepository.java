package com.adi.cms.gateway.repository;

import com.adi.cms.gateway.domain.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends ReactiveCrudRepository<Note, Long>, NoteRepositoryInternal {
    Flux<Note> findAllBy(Pageable pageable);

    @Query("SELECT * FROM note entity WHERE entity.xaracter_id_id = :id")
    Flux<Note> findByXaracterId(Long id);

    @Query("SELECT * FROM note entity WHERE entity.xaracter_id_id IS NULL")
    Flux<Note> findAllWhereXaracterIdIsNull();

    @Override
    <S extends Note> Mono<S> save(S entity);

    @Override
    Flux<Note> findAll();

    @Override
    Mono<Note> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface NoteRepositoryInternal {
    <S extends Note> Mono<S> save(S entity);

    Flux<Note> findAllBy(Pageable pageable);

    Flux<Note> findAll();

    Mono<Note> findById(Long id);

    Flux<Note> findAllBy(Pageable pageable, Criteria criteria);
}
