package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.CharacterEquippedArmor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CharacterEquippedArmor}, with proper type conversions.
 */
@Service
public class CharacterEquippedArmorRowMapper implements BiFunction<Row, String, CharacterEquippedArmor> {

    private final ColumnConverter converter;

    public CharacterEquippedArmorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CharacterEquippedArmor} stored in the database.
     */
    @Override
    public CharacterEquippedArmor apply(Row row, String prefix) {
        CharacterEquippedArmor entity = new CharacterEquippedArmor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        entity.setArmorPieceId(converter.fromRow(row, prefix + "_armor_piece_id", Long.class));
        return entity;
    }
}
