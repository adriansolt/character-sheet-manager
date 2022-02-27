package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Item;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Item}, with proper type conversions.
 */
@Service
public class ItemRowMapper implements BiFunction<Row, String, Item> {

    private final ColumnConverter converter;

    public ItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Item} stored in the database.
     */
    @Override
    public Item apply(Row row, String prefix) {
        Item entity = new Item();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Integer.class));
        entity.setQuality(converter.fromRow(row, prefix + "_quality", Integer.class));
        entity.setPictureContentType(converter.fromRow(row, prefix + "_picture_content_type", String.class));
        entity.setPicture(converter.fromRow(row, prefix + "_picture", byte[].class));
        entity.setCampaignId(converter.fromRow(row, prefix + "_campaign_id", Long.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        return entity;
    }
}
