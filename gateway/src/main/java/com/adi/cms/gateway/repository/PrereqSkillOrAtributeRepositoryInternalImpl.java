package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.PrereqSkillOrAtribute;
import com.adi.cms.gateway.repository.rowmapper.PrereqSkillOrAtributeRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the PrereqSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
class PrereqSkillOrAtributeRepositoryInternalImpl
    extends SimpleR2dbcRepository<PrereqSkillOrAtribute, Long>
    implements PrereqSkillOrAtributeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SkillRowMapper skillMapper;
    private final PrereqSkillOrAtributeRowMapper prereqskilloratributeMapper;

    private static final Table entityTable = Table.aliased("prereq_skill_or_atribute", EntityManager.ENTITY_ALIAS);
    private static final Table skillTable = Table.aliased("skill", "skill");

    public PrereqSkillOrAtributeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SkillRowMapper skillMapper,
        PrereqSkillOrAtributeRowMapper prereqskilloratributeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PrereqSkillOrAtribute.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.skillMapper = skillMapper;
        this.prereqskilloratributeMapper = prereqskilloratributeMapper;
    }

    @Override
    public Flux<PrereqSkillOrAtribute> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<PrereqSkillOrAtribute> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<PrereqSkillOrAtribute> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PrereqSkillOrAtributeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SkillSqlHelper.getColumns(skillTable, "skill"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(skillTable)
            .on(Column.create("skill_id", entityTable))
            .equals(Column.create("id", skillTable));

        String select = entityManager.createSelect(selectFrom, PrereqSkillOrAtribute.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PrereqSkillOrAtribute> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<PrereqSkillOrAtribute> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private PrereqSkillOrAtribute process(Row row, RowMetadata metadata) {
        PrereqSkillOrAtribute entity = prereqskilloratributeMapper.apply(row, "e");
        entity.setSkill(skillMapper.apply(row, "skill"));
        return entity;
    }

    @Override
    public <S extends PrereqSkillOrAtribute> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
