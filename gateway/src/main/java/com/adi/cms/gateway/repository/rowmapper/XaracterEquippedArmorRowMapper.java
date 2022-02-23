package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link XaracterEquippedArmor}, with proper type conversions.
 */
@Service
public class XaracterEquippedArmorRowMapper implements BiFunction<Row, String, XaracterEquippedArmor> {

    private final ColumnConverter converter;

    public XaracterEquippedArmorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link XaracterEquippedArmor} stored in the database.
     */
    @Override
    public XaracterEquippedArmor apply(Row row, String prefix) {
        XaracterEquippedArmor entity = new XaracterEquippedArmor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setXaracterId(converter.fromRow(row, prefix + "_xaracter_id", Long.class));
        entity.setArmorPieceId(converter.fromRow(row, prefix + "_armor_piece_id", Long.class));
        return entity;
    }
}
