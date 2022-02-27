package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.ArmorPiece;
import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import com.adi.cms.gateway.repository.rowmapper.ArmorPieceRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CampaignRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CharacterRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the ArmorPiece entity.
 */
@SuppressWarnings("unused")
class ArmorPieceRepositoryInternalImpl extends SimpleR2dbcRepository<ArmorPiece, Long> implements ArmorPieceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CampaignRowMapper campaignMapper;
    private final CharacterRowMapper characterMapper;
    private final ArmorPieceRowMapper armorpieceMapper;

    private static final Table entityTable = Table.aliased("armor_piece", EntityManager.ENTITY_ALIAS);
    private static final Table campaignTable = Table.aliased("campaign", "campaign");
    private static final Table characterTable = Table.aliased("character", "e_character");

    public ArmorPieceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CampaignRowMapper campaignMapper,
        CharacterRowMapper characterMapper,
        ArmorPieceRowMapper armorpieceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ArmorPiece.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.campaignMapper = campaignMapper;
        this.characterMapper = characterMapper;
        this.armorpieceMapper = armorpieceMapper;
    }

    @Override
    public Flux<ArmorPiece> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<ArmorPiece> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<ArmorPiece> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ArmorPieceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
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

        String select = entityManager.createSelect(selectFrom, ArmorPiece.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ArmorPiece> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<ArmorPiece> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private ArmorPiece process(Row row, RowMetadata metadata) {
        ArmorPiece entity = armorpieceMapper.apply(row, "e");
        entity.setCampaign(campaignMapper.apply(row, "campaign"));
        entity.setCharacter(characterMapper.apply(row, "character"));
        return entity;
    }

    @Override
    public <S extends ArmorPiece> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
