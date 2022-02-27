package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.ArmorPiece;
import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ArmorPiece}, with proper type conversions.
 */
@Service
public class ArmorPieceRowMapper implements BiFunction<Row, String, ArmorPiece> {

    private final ColumnConverter converter;

    public ArmorPieceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ArmorPiece} stored in the database.
     */
    @Override
    public ArmorPiece apply(Row row, String prefix) {
        ArmorPiece entity = new ArmorPiece();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Integer.class));
        entity.setQuality(converter.fromRow(row, prefix + "_quality", Integer.class));
        entity.setPictureContentType(converter.fromRow(row, prefix + "_picture_content_type", String.class));
        entity.setPicture(converter.fromRow(row, prefix + "_picture", byte[].class));
        entity.setLocation(converter.fromRow(row, prefix + "_location", ArmorLocation.class));
        entity.setDefenseModifier(converter.fromRow(row, prefix + "_defense_modifier", Integer.class));
        entity.setCampaignId(converter.fromRow(row, prefix + "_campaign_id", Long.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        return entity;
    }
}
