package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.WeaponManeuver;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WeaponManeuver}, with proper type conversions.
 */
@Service
public class WeaponManeuverRowMapper implements BiFunction<Row, String, WeaponManeuver> {

    private final ColumnConverter converter;

    public WeaponManeuverRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WeaponManeuver} stored in the database.
     */
    @Override
    public WeaponManeuver apply(Row row, String prefix) {
        WeaponManeuver entity = new WeaponManeuver();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setWeaponIdId(converter.fromRow(row, prefix + "_weapon_id_id", Long.class));
        entity.setManeuverIdId(converter.fromRow(row, prefix + "_maneuver_id_id", Long.class));
        return entity;
    }
}
