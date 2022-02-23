package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.XaracterAttribute;
import com.adi.cms.gateway.domain.enumeration.AttributeName;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link XaracterAttribute}, with proper type conversions.
 */
@Service
public class XaracterAttributeRowMapper implements BiFunction<Row, String, XaracterAttribute> {

    private final ColumnConverter converter;

    public XaracterAttributeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link XaracterAttribute} stored in the database.
     */
    @Override
    public XaracterAttribute apply(Row row, String prefix) {
        XaracterAttribute entity = new XaracterAttribute();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", AttributeName.class));
        entity.setPoints(converter.fromRow(row, prefix + "_points", Integer.class));
        entity.setAttributeModifier(converter.fromRow(row, prefix + "_attribute_modifier", Integer.class));
        entity.setXaracterIdId(converter.fromRow(row, prefix + "_xaracter_id_id", Long.class));
        return entity;
    }
}
