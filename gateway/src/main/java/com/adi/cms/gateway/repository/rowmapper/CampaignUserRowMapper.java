package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.CampaignUser;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CampaignUser}, with proper type conversions.
 */
@Service
public class CampaignUserRowMapper implements BiFunction<Row, String, CampaignUser> {

    private final ColumnConverter converter;

    public CampaignUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CampaignUser} stored in the database.
     */
    @Override
    public CampaignUser apply(Row row, String prefix) {
        CampaignUser entity = new CampaignUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCampaignId(converter.fromRow(row, prefix + "_campaign_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        return entity;
    }
}
