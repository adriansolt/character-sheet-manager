package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.Note;
import com.adi.cms.gateway.repository.rowmapper.CharacterRowMapper;
import com.adi.cms.gateway.repository.rowmapper.NoteRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Note entity.
 */
@SuppressWarnings("unused")
class NoteRepositoryInternalImpl extends SimpleR2dbcRepository<Note, Long> implements NoteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CharacterRowMapper characterMapper;
    private final NoteRowMapper noteMapper;

    private static final Table entityTable = Table.aliased("note", EntityManager.ENTITY_ALIAS);
    private static final Table characterTable = Table.aliased("character", "e_character");

    public NoteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CharacterRowMapper characterMapper,
        NoteRowMapper noteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Note.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.characterMapper = characterMapper;
        this.noteMapper = noteMapper;
    }

    @Override
    public Flux<Note> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Note> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Note> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = NoteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CharacterSqlHelper.getColumns(characterTable, "character"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(characterTable)
            .on(Column.create("character_id", entityTable))
            .equals(Column.create("id", characterTable));

        String select = entityManager.createSelect(selectFrom, Note.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Note> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Note> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private Note process(Row row, RowMetadata metadata) {
        Note entity = noteMapper.apply(row, "e");
        entity.setCharacter(characterMapper.apply(row, "character"));
        return entity;
    }

    @Override
    public <S extends Note> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
