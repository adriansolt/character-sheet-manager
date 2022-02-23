package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.XaracterAttribute;
import com.adi.cms.gateway.domain.enumeration.AttributeName;
import com.adi.cms.gateway.repository.rowmapper.XaracterAttributeRowMapper;
import com.adi.cms.gateway.repository.rowmapper.XaracterRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the XaracterAttribute entity.
 */
@SuppressWarnings("unused")
class XaracterAttributeRepositoryInternalImpl
    extends SimpleR2dbcRepository<XaracterAttribute, Long>
    implements XaracterAttributeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final XaracterRowMapper xaracterMapper;
    private final XaracterAttributeRowMapper xaracterattributeMapper;

    private static final Table entityTable = Table.aliased("xaracter_attribute", EntityManager.ENTITY_ALIAS);
    private static final Table xaracterIdTable = Table.aliased("xaracter", "xaracterId");

    public XaracterAttributeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        XaracterRowMapper xaracterMapper,
        XaracterAttributeRowMapper xaracterattributeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(XaracterAttribute.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.xaracterMapper = xaracterMapper;
        this.xaracterattributeMapper = xaracterattributeMapper;
    }

    @Override
    public Flux<XaracterAttribute> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<XaracterAttribute> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<XaracterAttribute> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = XaracterAttributeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(XaracterSqlHelper.getColumns(xaracterIdTable, "xaracterId"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(xaracterIdTable)
            .on(Column.create("xaracter_id_id", entityTable))
            .equals(Column.create("id", xaracterIdTable));

        String select = entityManager.createSelect(selectFrom, XaracterAttribute.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<XaracterAttribute> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<XaracterAttribute> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private XaracterAttribute process(Row row, RowMetadata metadata) {
        XaracterAttribute entity = xaracterattributeMapper.apply(row, "e");
        entity.setXaracterId(xaracterMapper.apply(row, "xaracterId"));
        return entity;
    }

    @Override
    public <S extends XaracterAttribute> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
