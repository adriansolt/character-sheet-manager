package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.rowmapper.CharacterEquippedWeaponRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the CharacterEquippedWeapon entity.
 */
@SuppressWarnings("unused")
class CharacterEquippedWeaponRepositoryInternalImpl
    extends SimpleR2dbcRepository<CharacterEquippedWeapon, Long>
    implements CharacterEquippedWeaponRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WeaponRowMapper weaponMapper;
    private final CharacterEquippedWeaponRowMapper characterequippedweaponMapper;

    private static final Table entityTable = Table.aliased("character_equipped_weapon", EntityManager.ENTITY_ALIAS);
    private static final Table weaponTable = Table.aliased("weapon", "weapon");

    public CharacterEquippedWeaponRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WeaponRowMapper weaponMapper,
        CharacterEquippedWeaponRowMapper characterequippedweaponMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(CharacterEquippedWeapon.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.weaponMapper = weaponMapper;
        this.characterequippedweaponMapper = characterequippedweaponMapper;
    }

    @Override
    public Flux<CharacterEquippedWeapon> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<CharacterEquippedWeapon> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<CharacterEquippedWeapon> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CharacterEquippedWeaponSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WeaponSqlHelper.getColumns(weaponTable, "weapon"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(weaponTable)
            .on(Column.create("weapon_id", entityTable))
            .equals(Column.create("id", weaponTable));

        String select = entityManager.createSelect(selectFrom, CharacterEquippedWeapon.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CharacterEquippedWeapon> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<CharacterEquippedWeapon> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private CharacterEquippedWeapon process(Row row, RowMetadata metadata) {
        CharacterEquippedWeapon entity = characterequippedweaponMapper.apply(row, "e");
        entity.setWeapon(weaponMapper.apply(row, "weapon"));
        return entity;
    }

    @Override
    public <S extends CharacterEquippedWeapon> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
