package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Maneuver;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Maneuver}, with proper type conversions.
 */
@Service
public class ManeuverRowMapper implements BiFunction<Row, String, Maneuver> {

    private final ColumnConverter converter;

    public ManeuverRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Maneuver} stored in the database.
     */
    @Override
    public Maneuver apply(Row row, String prefix) {
        Maneuver entity = new Maneuver();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setModifier(converter.fromRow(row, prefix + "_modifier", Integer.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
