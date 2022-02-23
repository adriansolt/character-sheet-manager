package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.CharacterSkill;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CharacterSkill}, with proper type conversions.
 */
@Service
public class CharacterSkillRowMapper implements BiFunction<Row, String, CharacterSkill> {

    private final ColumnConverter converter;

    public CharacterSkillRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CharacterSkill} stored in the database.
     */
    @Override
    public CharacterSkill apply(Row row, String prefix) {
        CharacterSkill entity = new CharacterSkill();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPoints(converter.fromRow(row, prefix + "_points", Integer.class));
        entity.setSkillModifier(converter.fromRow(row, prefix + "_skill_modifier", Integer.class));
        entity.setCharacterId(converter.fromRow(row, prefix + "_character_id", Long.class));
        entity.setSkillId(converter.fromRow(row, prefix + "_skill_id", Long.class));
        return entity;
    }
}
