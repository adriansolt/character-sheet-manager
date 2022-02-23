package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.WeaponManeuver;
import com.adi.cms.gateway.repository.rowmapper.ManeuverRowMapper;
import com.adi.cms.gateway.repository.rowmapper.WeaponManeuverRowMapper;
import com.adi.cms.gateway.repository.rowmapper.WeaponRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the WeaponManeuver entity.
 */
@SuppressWarnings("unused")
class WeaponManeuverRepositoryInternalImpl extends SimpleR2dbcRepository<WeaponManeuver, Long> implements WeaponManeuverRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WeaponRowMapper weaponMapper;
    private final ManeuverRowMapper maneuverMapper;
    private final WeaponManeuverRowMapper weaponmaneuverMapper;

    private static final Table entityTable = Table.aliased("weapon_maneuver", EntityManager.ENTITY_ALIAS);
    private static final Table weaponIdTable = Table.aliased("weapon", "weaponId");
    private static final Table maneuverIdTable = Table.aliased("maneuver", "maneuverId");

    public WeaponManeuverRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WeaponRowMapper weaponMapper,
        ManeuverRowMapper maneuverMapper,
        WeaponManeuverRowMapper weaponmaneuverMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WeaponManeuver.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.weaponMapper = weaponMapper;
        this.maneuverMapper = maneuverMapper;
        this.weaponmaneuverMapper = weaponmaneuverMapper;
    }

    @Override
    public Flux<WeaponManeuver> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<WeaponManeuver> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<WeaponManeuver> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = WeaponManeuverSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WeaponSqlHelper.getColumns(weaponIdTable, "weaponId"));
        columns.addAll(ManeuverSqlHelper.getColumns(maneuverIdTable, "maneuverId"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(weaponIdTable)
            .on(Column.create("weapon_id_id", entityTable))
            .equals(Column.create("id", weaponIdTable))
            .leftOuterJoin(maneuverIdTable)
            .on(Column.create("maneuver_id_id", entityTable))
            .equals(Column.create("id", maneuverIdTable));

        String select = entityManager.createSelect(selectFrom, WeaponManeuver.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WeaponManeuver> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<WeaponManeuver> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private WeaponManeuver process(Row row, RowMetadata metadata) {
        WeaponManeuver entity = weaponmaneuverMapper.apply(row, "e");
        entity.setWeaponId(weaponMapper.apply(row, "weaponId"));
        entity.setManeuverId(maneuverMapper.apply(row, "maneuverId"));
        return entity;
    }

    @Override
    public <S extends WeaponManeuver> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
