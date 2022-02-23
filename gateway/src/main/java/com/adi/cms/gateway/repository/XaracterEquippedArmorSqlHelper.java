package com.adi.cms.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class XaracterEquippedArmorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("xaracter_id", table, columnPrefix + "_xaracter_id"));

        columns.add(Column.aliased("armor_piece_id", table, columnPrefix + "_armor_piece_id"));
        return columns;
    }
}
