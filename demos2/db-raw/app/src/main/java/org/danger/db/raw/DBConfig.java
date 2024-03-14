package org.danger.db.raw;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"host", "port", "dbname"})
public class DBConfig {

    private String host;
    private int port;
    private String dbname;
    private String username;
    private String password;


}
