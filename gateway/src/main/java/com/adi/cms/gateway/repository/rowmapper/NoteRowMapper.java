package com.adi.cms.gateway.repository.rowmapper;

import com.adi.cms.gateway.domain.Note;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Note}, with proper type conversions.
 */
@Service
public class NoteRowMapper implements BiFunction<Row, String, Note> {

    private final ColumnConverter converter;

    public NoteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Note} stored in the database.
     */
    @Override
    public Note apply(Row row, String prefix) {
        Note entity = new Note();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setXaracterIdId(converter.fromRow(row, prefix + "_xaracter_id_id", Long.class));
        return entity;
    }
}
