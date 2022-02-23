package com.adi.cms.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.adi.cms.gateway.domain.CampaignUser;
import com.adi.cms.gateway.repository.rowmapper.CampaignRowMapper;
import com.adi.cms.gateway.repository.rowmapper.CampaignUserRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the CampaignUser entity.
 */
@SuppressWarnings("unused")
class CampaignUserRepositoryInternalImpl extends SimpleR2dbcRepository<CampaignUser, Long> implements CampaignUserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CampaignRowMapper campaignMapper;
    private final UserRowMapper userMapper;
    private final CampaignUserRowMapper campaignuserMapper;

    private static final Table entityTable = Table.aliased("campaign_user", EntityManager.ENTITY_ALIAS);
    private static final Table campaignIdTable = Table.aliased("campaign", "campaignId");
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public CampaignUserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CampaignRowMapper campaignMapper,
        UserRowMapper userMapper,
        CampaignUserRowMapper campaignuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CampaignUser.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.campaignMapper = campaignMapper;
        this.userMapper = userMapper;
        this.campaignuserMapper = campaignuserMapper;
    }

    @Override
    public Flux<CampaignUser> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<CampaignUser> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<CampaignUser> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CampaignUserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CampaignSqlHelper.getColumns(campaignIdTable, "campaignId"));
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(campaignIdTable)
            .on(Column.create("campaign_id_id", entityTable))
            .equals(Column.create("id", campaignIdTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));

        String select = entityManager.createSelect(selectFrom, CampaignUser.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CampaignUser> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<CampaignUser> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private CampaignUser process(Row row, RowMetadata metadata) {
        CampaignUser entity = campaignuserMapper.apply(row, "e");
        entity.setCampaignId(campaignMapper.apply(row, "campaignId"));
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends CampaignUser> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
