package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.XaracterSkill;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link XaracterSkill}, with proper type conversions.
 */
@Service
public class XaracterSkillRowMapper implements BiFunction<Row, String, XaracterSkill> {

    private final ColumnConverter converter;

    public XaracterSkillRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link XaracterSkill} stored in the database.
     */
    @Override
    public XaracterSkill apply(Row row, String prefix) {
        XaracterSkill entity = new XaracterSkill();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPoints(converter.fromRow(row, prefix + "_points", Integer.class));
        entity.setSkillModifier(converter.fromRow(row, prefix + "_skill_modifier", Integer.class));
        entity.setXaracterIdId(converter.fromRow(row, prefix + "_xaracter_id_id", Long.class));
        entity.setSkillIdId(converter.fromRow(row, prefix + "_skill_id_id", Long.class));
        return entity;
    }
}
