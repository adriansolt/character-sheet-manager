package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Campaign;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Campaign}, with proper type conversions.
 */
@Service
public class CampaignRowMapper implements BiFunction<Row, String, Campaign> {

    private final ColumnConverter converter;

    public CampaignRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Campaign} stored in the database.
     */
    @Override
    public Campaign apply(Row row, String prefix) {
        Campaign entity = new Campaign();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setMapContentType(converter.fromRow(row, prefix + "_map_content_type", String.class));
        entity.setMap(converter.fromRow(row, prefix + "_map", byte[].class));
        entity.setMasterId(converter.fromRow(row, prefix + "_master_id", Long.class));
        return entity;
    }
}
