package com.geesatcom.blog.bootstrap;


import java.util.HashMap;
import java.util.Map;

import com.geesatcom.blog.bootstrap.config.MySQLProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope
public class ConfigController {

    @Autowired
    private MySQLProperties mySQLProperties;
    @Autowired
    private ConsulYamlService consulYamlService;

    @Value("${name}")
    private String name;

    @Value("${name2}")
    private String name2;

    @GetMapping("/name")
    public String getName() {
        return name;
    }

    @GetMapping("/get")
    public Map get() {
        Map ret = new HashMap();
        ret.put("name2", name2);
        ret.put("mySQLProperties", mySQLProperties);
        return ret;
    }

    @GetMapping("/update")
    public MySQLProperties update() {
        mySQLProperties.setPort(mySQLProperties.getPort() + 1);
        modifyConfigCenter(mySQLProperties);
        return mySQLProperties;
    }

    private void modifyConfigCenter(MySQLProperties properties) {
        String configPath = consulYamlService.getDefaultConsulKVPath();
        consulYamlService.updateYamlInPath(configPath,
                consulYamlService.getPrefixByAnnotationConfigurationProperties(properties),
                properties);
    }
}