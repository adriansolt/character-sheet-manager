package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import com.adi.cms.gateway.repository.rowmapper.DefaultSkillOrAtributeRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the DefaultSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
class DefaultSkillOrAtributeRepositoryInternalImpl
    extends SimpleR2dbcRepository<DefaultSkillOrAtribute, Long>
    implements DefaultSkillOrAtributeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SkillRowMapper skillMapper;
    private final DefaultSkillOrAtributeRowMapper defaultskilloratributeMapper;

    private static final Table entityTable = Table.aliased("default_skill_or_atribute", EntityManager.ENTITY_ALIAS);
    private static final Table skillIdTable = Table.aliased("skill", "skillId");

    public DefaultSkillOrAtributeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SkillRowMapper skillMapper,
        DefaultSkillOrAtributeRowMapper defaultskilloratributeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DefaultSkillOrAtribute.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.skillMapper = skillMapper;
        this.defaultskilloratributeMapper = defaultskilloratributeMapper;
    }

    @Override
    public Flux<DefaultSkillOrAtribute> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<DefaultSkillOrAtribute> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<DefaultSkillOrAtribute> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DefaultSkillOrAtributeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SkillSqlHelper.getColumns(skillIdTable, "skillId"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(skillIdTable)
            .on(Column.create("skill_id_id", entityTable))
            .equals(Column.create("id", skillIdTable));

        String select = entityManager.createSelect(selectFrom, DefaultSkillOrAtribute.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DefaultSkillOrAtribute> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<DefaultSkillOrAtribute> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private DefaultSkillOrAtribute process(Row row, RowMetadata metadata) {
        DefaultSkillOrAtribute entity = defaultskilloratributeMapper.apply(row, "e");
        entity.setSkillId(skillMapper.apply(row, "skillId"));
        return entity;
    }

    @Override
    public <S extends DefaultSkillOrAtribute> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
