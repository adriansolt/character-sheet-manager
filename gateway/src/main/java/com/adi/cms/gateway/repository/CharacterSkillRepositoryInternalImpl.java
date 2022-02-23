package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.CharacterSkill;
import com.adi.cms.gateway.repository.rowmapper.CharacterRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CharacterSkillRowMapper;
import com.adi.cms.gateway.repository.rowmapper.SkillRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the CharacterSkill entity.
 */
@SuppressWarnings("unused")
class CharacterSkillRepositoryInternalImpl extends SimpleR2dbcRepository<CharacterSkill, Long> implements CharacterSkillRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CharacterRowMapper characterMapper;
    private final SkillRowMapper skillMapper;
    private final CharacterSkillRowMapper characterskillMapper;

    private static final Table entityTable = Table.aliased("character_skill", EntityManager.ENTITY_ALIAS);
    private static final Table characterTable = Table.aliased("character", "e_character");
    private static final Table skillTable = Table.aliased("skill", "skill");

    public CharacterSkillRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CharacterRowMapper characterMapper,
        SkillRowMapper skillMapper,
        CharacterSkillRowMapper characterskillMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CharacterSkill.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.characterMapper = characterMapper;
        this.skillMapper = skillMapper;
        this.characterskillMapper = characterskillMapper;
    }

    @Override
    public Flux<CharacterSkill> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<CharacterSkill> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<CharacterSkill> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CharacterSkillSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CharacterSqlHelper.getColumns(characterTable, "character"));
        columns.addAll(SkillSqlHelper.getColumns(skillTable, "skill"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(characterTable)
            .on(Column.create("character_id", entityTable))
            .equals(Column.create("id", characterTable))
            .leftOuterJoin(skillTable)
            .on(Column.create("skill_id", entityTable))
            .equals(Column.create("id", skillTable));

        String select = entityManager.createSelect(selectFrom, CharacterSkill.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CharacterSkill> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<CharacterSkill> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private CharacterSkill process(Row row, RowMetadata metadata) {
        CharacterSkill entity = characterskillMapper.apply(row, "e");
        entity.setCharacter(characterMapper.apply(row, "character"));
        entity.setSkill(skillMapper.apply(row, "skill"));
        return entity;
    }

    @Override
    public <S extends CharacterSkill> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
