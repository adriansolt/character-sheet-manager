package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import com.adi.cms.gateway.repository.rowmapper.ArmorPieceRowMapper;
import com.adi.cms.gateway.repository.rowmapper.XaracterEquippedArmorRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the XaracterEquippedArmor entity.
 */
@SuppressWarnings("unused")
class XaracterEquippedArmorRepositoryInternalImpl
    extends SimpleR2dbcRepository<XaracterEquippedArmor, Long>
    implements XaracterEquippedArmorRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ArmorPieceRowMapper armorpieceMapper;
    private final XaracterEquippedArmorRowMapper xaracterequippedarmorMapper;

    private static final Table entityTable = Table.aliased("xaracter_equipped_armor", EntityManager.ENTITY_ALIAS);
    private static final Table armorPieceTable = Table.aliased("armor_piece", "armorPiece");

    public XaracterEquippedArmorRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ArmorPieceRowMapper armorpieceMapper,
        XaracterEquippedArmorRowMapper xaracterequippedarmorMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(XaracterEquippedArmor.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.armorpieceMapper = armorpieceMapper;
        this.xaracterequippedarmorMapper = xaracterequippedarmorMapper;
    }

    @Override
    public Flux<XaracterEquippedArmor> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<XaracterEquippedArmor> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<XaracterEquippedArmor> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = XaracterEquippedArmorSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ArmorPieceSqlHelper.getColumns(armorPieceTable, "armorPiece"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(armorPieceTable)
            .on(Column.create("armor_piece_id", entityTable))
            .equals(Column.create("id", armorPieceTable));

        String select = entityManager.createSelect(selectFrom, XaracterEquippedArmor.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<XaracterEquippedArmor> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<XaracterEquippedArmor> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private XaracterEquippedArmor process(Row row, RowMetadata metadata) {
        XaracterEquippedArmor entity = xaracterequippedarmorMapper.apply(row, "e");
        entity.setArmorPiece(armorpieceMapper.apply(row, "armorPiece"));
        return entity;
    }

    @Override
    public <S extends XaracterEquippedArmor> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
