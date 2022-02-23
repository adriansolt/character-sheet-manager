package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Weapon;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Weapon}, with proper type conversions.
 */
@Service
public class WeaponRowMapper implements BiFunction<Row, String, Weapon> {

    private final ColumnConverter converter;

    public WeaponRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Weapon} stored in the database.
     */
    @Override
    public Weapon apply(Row row, String prefix) {
        Weapon entity = new Weapon();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setReach(converter.fromRow(row, prefix + "_reach", Integer.class));
        entity.setBaseDamage(converter.fromRow(row, prefix + "_base_damage", Integer.class));
        entity.setRequiredST(converter.fromRow(row, prefix + "_required_st", Integer.class));
        entity.setDamageModifier(converter.fromRow(row, prefix + "_damage_modifier", Integer.class));
        return entity;
    }
}
