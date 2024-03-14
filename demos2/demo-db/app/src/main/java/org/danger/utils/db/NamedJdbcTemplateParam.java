package org.danger.utils.db;


import lombok.Data;
import org.danger.utils.AssocArray;

@Data

public class NamedJdbcTemplateParam {

    private String sql;
    private AssocArray params;

    public NamedJdbcTemplateParam(String sql, AssocArray params) {
        this.sql = sql;
        this.params = params;
    }

}
