package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.PrereqSkillOrAtribute;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PrereqSkillOrAtribute}, with proper type conversions.
 */
@Service
public class PrereqSkillOrAtributeRowMapper implements BiFunction<Row, String, PrereqSkillOrAtribute> {

    private final ColumnConverter converter;

    public PrereqSkillOrAtributeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PrereqSkillOrAtribute} stored in the database.
     */
    @Override
    public PrereqSkillOrAtribute apply(Row row, String prefix) {
        PrereqSkillOrAtribute entity = new PrereqSkillOrAtribute();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setSkillIdId(converter.fromRow(row, prefix + "_skill_id_id", Long.class));
        return entity;
    }
}
