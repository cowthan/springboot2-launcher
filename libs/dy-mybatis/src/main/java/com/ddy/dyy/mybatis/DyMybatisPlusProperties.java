package com.ddy.dyy.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mybatis-plus")
@Getter @Setter
public class DyMybatisPlusProperties {
    protected boolean logEnable;
    protected String logIgnoreKeywords;
    protected String logLevel = "info";
}
