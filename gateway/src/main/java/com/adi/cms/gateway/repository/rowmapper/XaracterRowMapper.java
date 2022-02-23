package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Xaracter;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Xaracter}, with proper type conversions.
 */
@Service
public class XaracterRowMapper implements BiFunction<Row, String, Xaracter> {

    private final ColumnConverter converter;

    public XaracterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Xaracter} stored in the database.
     */
    @Override
    public Xaracter apply(Row row, String prefix) {
        Xaracter entity = new Xaracter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Integer.class));
        entity.setHeight(converter.fromRow(row, prefix + "_height", Integer.class));
        entity.setPoints(converter.fromRow(row, prefix + "_points", Integer.class));
        entity.setPictureContentType(converter.fromRow(row, prefix + "_picture_content_type", String.class));
        entity.setPicture(converter.fromRow(row, prefix + "_picture", byte[].class));
        entity.setHandedness(converter.fromRow(row, prefix + "_handedness", Handedness.class));
        entity.setCampaignId(converter.fromRow(row, prefix + "_campaign_id", Long.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        return entity;
    }
}
