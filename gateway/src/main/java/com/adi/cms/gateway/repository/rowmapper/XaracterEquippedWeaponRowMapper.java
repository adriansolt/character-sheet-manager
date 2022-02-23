package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.XaracterEquippedWeapon;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link XaracterEquippedWeapon}, with proper type conversions.
 */
@Service
public class XaracterEquippedWeaponRowMapper implements BiFunction<Row, String, XaracterEquippedWeapon> {

    private final ColumnConverter converter;

    public XaracterEquippedWeaponRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link XaracterEquippedWeapon} stored in the database.
     */
    @Override
    public XaracterEquippedWeapon apply(Row row, String prefix) {
        XaracterEquippedWeapon entity = new XaracterEquippedWeapon();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setXaracterId(converter.fromRow(row, prefix + "_xaracter_id", Long.class));
        entity.setHand(converter.fromRow(row, prefix + "_hand", Handedness.class));
        entity.setWeaponIdId(converter.fromRow(row, prefix + "_weapon_id_id", Long.class));
        return entity;
    }
}
