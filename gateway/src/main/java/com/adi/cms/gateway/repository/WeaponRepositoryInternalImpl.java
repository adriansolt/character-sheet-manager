package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.Weapon;
import com.adi.cms.gateway.repository.rowmapper.CampaignRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CharacterRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Weapon entity.
 */
@SuppressWarnings("unused")
class WeaponRepositoryInternalImpl extends SimpleR2dbcRepository<Weapon, Long> implements WeaponRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CampaignRowMapper campaignMapper;
    private final CharacterRowMapper characterMapper;
    private final WeaponRowMapper weaponMapper;

    private static final Table entityTable = Table.aliased("weapon", EntityManager.ENTITY_ALIAS);
    private static final Table campaignTable = Table.aliased("campaign", "campaign");
    private static final Table characterTable = Table.aliased("character", "e_character");

    public WeaponRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CampaignRowMapper campaignMapper,
        CharacterRowMapper characterMapper,
        WeaponRowMapper weaponMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Weapon.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.campaignMapper = campaignMapper;
        this.characterMapper = characterMapper;
        this.weaponMapper = weaponMapper;
    }

    @Override
    public Flux<Weapon> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Weapon> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Weapon> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = WeaponSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CampaignSqlHelper.getColumns(campaignTable, "campaign"));
        columns.addAll(CharacterSqlHelper.getColumns(characterTable, "character"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(campaignTable)
            .on(Column.create("campaign_id", entityTable))
            .equals(Column.create("id", campaignTable))
            .leftOuterJoin(characterTable)
            .on(Column.create("character_id", entityTable))
            .equals(Column.create("id", characterTable));

        String select = entityManager.createSelect(selectFrom, Weapon.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Weapon> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Weapon> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private Weapon process(Row row, RowMetadata metadata) {
        Weapon entity = weaponMapper.apply(row, "e");
        entity.setCampaign(campaignMapper.apply(row, "campaign"));
        entity.setCharacter(characterMapper.apply(row, "character"));
        return entity;
    }

    @Override
    public <S extends Weapon> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
