package com.adi.cms.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CharacterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        columns.add(Column.aliased("height", table, columnPrefix + "_height"));
        columns.add(Column.aliased("points", table, columnPrefix + "_points"));
        columns.add(Column.aliased("picture", table, columnPrefix + "_picture"));
        columns.add(Column.aliased("picture_content_type", table, columnPrefix + "_picture_content_type"));
        columns.add(Column.aliased("handedness", table, columnPrefix + "_handedness"));
        columns.add(Column.aliased("active", table, columnPrefix + "_active"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("campaign_id", table, columnPrefix + "_campaign_id"));
        return columns;
    }
}
