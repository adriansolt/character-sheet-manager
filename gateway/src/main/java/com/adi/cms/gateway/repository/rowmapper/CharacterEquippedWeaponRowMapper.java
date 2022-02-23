package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CharacterEquippedWeapon}, with proper type conversions.
 */
@Service
public class CharacterEquippedWeaponRowMapper implements BiFunction<Row, String, CharacterEquippedWeapon> {

    private final ColumnConverter converter;

    public CharacterEquippedWeaponRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CharacterEquippedWeapon} stored in the database.
     */
    @Override
    public CharacterEquippedWeapon apply(Row row, String prefix) {
        CharacterEquippedWeapon entity = new CharacterEquippedWeapon();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        entity.setHand(converter.fromRow(row, prefix + "_hand", Handedness.class));
        entity.setWeaponId(converter.fromRow(row, prefix + "_weapon_id", Long.class));
        return entity;
    }
}
