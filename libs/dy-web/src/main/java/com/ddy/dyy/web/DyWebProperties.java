package com.ddy.dyy.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dy.config.web")
@Getter @Setter
public class DyWebProperties {

    
    private String responseTimeFormat = "timestamps";
    
    private boolean responseLongToString = false;
    
    private boolean logEnable = false;
    
    private boolean restControllerErrorAdviceEnable = true;
    
    private String logIgnoreKeywords = "";
    
    private String logLevel = "";

    private String logMode = "interceptor"; // filter, interceptor, acpect，其中filter无法设置trace_id

    private boolean websocketEnable = false;
}
