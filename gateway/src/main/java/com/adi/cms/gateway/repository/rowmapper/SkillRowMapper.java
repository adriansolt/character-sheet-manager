package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Skill;
import com.adi.cms.gateway.domain.enumeration.Difficulty;
import com.adi.cms.gateway.domain.enumeration.SkillName;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Skill}, with proper type conversions.
 */
@Service
public class SkillRowMapper implements BiFunction<Row, String, Skill> {

    private final ColumnConverter converter;

    public SkillRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Skill} stored in the database.
     */
    @Override
    public Skill apply(Row row, String prefix) {
        Skill entity = new Skill();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", SkillName.class));
        entity.setDifficulty(converter.fromRow(row, prefix + "_difficulty", Difficulty.class));
        return entity;
    }
}
