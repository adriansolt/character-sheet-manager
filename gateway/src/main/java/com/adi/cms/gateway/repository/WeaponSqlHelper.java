package com.adi.cms.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class WeaponSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        columns.add(Column.aliased("quality", table, columnPrefix + "_quality"));
        columns.add(Column.aliased("picture", table, columnPrefix + "_picture"));
        columns.add(Column.aliased("picture_content_type", table, columnPrefix + "_picture_content_type"));
        columns.add(Column.aliased("reach", table, columnPrefix + "_reach"));
        columns.add(Column.aliased("base_damage", table, columnPrefix + "_base_damage"));
        columns.add(Column.aliased("required_st", table, columnPrefix + "_required_st"));
        columns.add(Column.aliased("damage_modifier", table, columnPrefix + "_damage_modifier"));

        columns.add(Column.aliased("character_id", table, columnPrefix + "_character_id"));
        return columns;
    }
}
