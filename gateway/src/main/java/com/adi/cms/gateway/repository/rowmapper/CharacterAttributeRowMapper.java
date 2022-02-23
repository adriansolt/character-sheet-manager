package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.CharacterAttribute;
import com.adi.cms.gateway.domain.enumeration.AttributeName;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CharacterAttribute}, with proper type conversions.
 */
@Service
public class CharacterAttributeRowMapper implements BiFunction<Row, String, CharacterAttribute> {

    private final ColumnConverter converter;

    public CharacterAttributeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CharacterAttribute} stored in the database.
     */
    @Override
    public CharacterAttribute apply(Row row, String prefix) {
        CharacterAttribute entity = new CharacterAttribute();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", AttributeName.class));
        entity.setPoints(converter.fromRow(row, prefix + "_points", Integer.class));
        entity.setAttributeModifier(converter.fromRow(row, prefix + "_attribute_modifier", Integer.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        return entity;
    }
}
