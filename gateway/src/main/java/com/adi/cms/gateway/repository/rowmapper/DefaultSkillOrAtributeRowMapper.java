package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DefaultSkillOrAtribute}, with proper type conversions.
 */
@Service
public class DefaultSkillOrAtributeRowMapper implements BiFunction<Row, String, DefaultSkillOrAtribute> {

    private final ColumnConverter converter;

    public DefaultSkillOrAtributeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DefaultSkillOrAtribute} stored in the database.
     */
    @Override
    public DefaultSkillOrAtribute apply(Row row, String prefix) {
        DefaultSkillOrAtribute entity = new DefaultSkillOrAtribute();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setModifier(converter.fromRow(row, prefix + "_modifier", Integer.class));
        entity.setSkillId(converter.fromRow(row, prefix + "_skill_id", Long.class));
        return entity;
    }
}
