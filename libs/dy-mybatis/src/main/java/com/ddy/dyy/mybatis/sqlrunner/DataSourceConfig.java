package com.ddy.dyy.mybatis.sqlrunner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String dbtype; //mysql、postgres、oracle等

    @Override
    public String toString() {
        return "DataSourceConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", dbtype='" + dbtype + '\'' +
                '}';
    }
}