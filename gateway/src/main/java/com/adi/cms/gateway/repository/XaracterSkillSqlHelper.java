package com.adi.cms.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class XaracterSkillSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("points", table, columnPrefix + "_points"));
        columns.add(Column.aliased("skill_modifier", table, columnPrefix + "_skill_modifier"));

        columns.add(Column.aliased("xaracter_id_id", table, columnPrefix + "_xaracter_id_id"));
        columns.add(Column.aliased("skill_id_id", table, columnPrefix + "_skill_id_id"));
        return columns;
    }
}
