package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.Character;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.rowmapper.CampaignRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CharacterRowMapper;
import com.adi.cms.gateway.repository.rowmapper.UserRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Character entity.
 */
@SuppressWarnings("unused")
class CharacterRepositoryInternalImpl extends SimpleR2dbcRepository<Character, Long> implements CharacterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final CampaignRowMapper campaignMapper;
    private final CharacterRowMapper characterMapper;

    private static final Table entityTable = Table.aliased("character", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table campaignTable = Table.aliased("campaign", "campaign");

    public CharacterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        CampaignRowMapper campaignMapper,
        CharacterRowMapper characterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Character.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.campaignMapper = campaignMapper;
        this.characterMapper = characterMapper;
    }

    @Override
    public Flux<Character> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Character> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Character> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CharacterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(CampaignSqlHelper.getColumns(campaignTable, "campaign"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(campaignTable)
            .on(Column.create("campaign_id", entityTable))
            .equals(Column.create("id", campaignTable));

        String select = entityManager.createSelect(selectFrom, Character.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Character> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Character> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private Character process(Row row, RowMetadata metadata) {
        Character entity = characterMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setCampaign(campaignMapper.apply(row, "campaign"));
        return entity;
    }

    @Override
    public <S extends Character> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
