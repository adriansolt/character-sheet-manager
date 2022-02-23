package com.adi.cms.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CharacterAttributeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("points", table, columnPrefix + "_points"));
        columns.add(Column.aliased("attribute_modifier", table, columnPrefix + "_attribute_modifier"));

        columns.add(Column.aliased("character_id", table, columnPrefix + "_character_id"));
        return columns;
    }
}
