package org.danger.db.raw;

import lombok.Data;

@Data

public class NamedJdbcTemplateParam {

    private String sql;
    private AssocArray params;

    public NamedJdbcTemplateParam(String sql, AssocArray params) {
        this.sql = sql;
        this.params = params;
    }

}
