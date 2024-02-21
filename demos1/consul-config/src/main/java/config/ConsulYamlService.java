package com.geesatcom.blog.bootstrap;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;

import com.ecwid.consul.v1.ConsulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * consul配置中心统一初始化、写入
 */
@Service
@Slf4j
public class ConsulYamlService {
    @Autowired
    private ConsulClient consulClient;
    @Autowired
    private ConsulConfigProperties consulConfigProperties;
    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        init(getDefaultConsulKVPath(), "application.yml");
        String[] profiles = environment.getActiveProfiles();
        for (String profile : profiles) {
            init(getProfilesConsulKVPath(profile), "application-" + profile + ".yml");
        }
    }

    public void init(String consulKVPath, String applicationYamlFile) {
        // TODO 如果consulKVPath对应的consul kv没有value，则读取applicationYamlFile的所有配置并写入consulKVPath
        Resource resource = new ClassPathResource(applicationYamlFile);
        String yaml = null;
        try {
            yaml = fileGetContent(resource.getInputStream());

            String oldValue = consulClient.getKVValue(consulKVPath).getValue().getValue();
            if (oldValue == null || oldValue.equals("")) {
                consulClient.setKVValue(consulKVPath, yaml);
            }
        } catch (FileNotFoundException e) {
            log.debug("FileNotFoundException-{}", resource.getFilename());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final int READ_BUFFER_LEN = 1024;
    private static final int INVALID_LEN = -1;

    private static String fileGetContent(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            byte[] buffer = new byte[READ_BUFFER_LEN];
            int len = 0;
            StringBuffer sb = new StringBuffer();
            while ((len = inputStream.read(buffer)) != INVALID_LEN) {
                sb.append(new String(buffer, 0, len));
            }
            inputStream.close();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultConsulKVPath() {
        String configPath = consulConfigProperties.getPrefix()
                + "/" + consulConfigProperties.getDefaultContext()
                + "/" + consulConfigProperties.getDataKey();
        return configPath;
    }

    public String getProfilesConsulKVPath(String profile) {
        String configPath = consulConfigProperties.getPrefix()
                + "/" + consulConfigProperties.getDefaultContext()
                + consulConfigProperties.getProfileSeparator()
                + profile
                + "/" + consulConfigProperties.getDataKey();
        return configPath;

//        String[] profiles = environment.getActiveProfiles();
//        List<String> ret = new ArrayList<>();
//        for (String profile : profiles) {
//            String configPath = consulConfigProperties.getPrefix()
//                    + "/" + consulConfigProperties.getDefaultContext() + "," + profile
//                    + "/" + consulConfigProperties.getDataKey();
//            ret.add(configPath);
//        }
//        return ret;
    }

    public String getYaml(String consulKVPath) {
        String oldValue = consulClient.getKVValue(consulKVPath).getValue().getValue();
        oldValue = oldValue == null ? "" : new String(Base64.getDecoder().decode(oldValue), StandardCharsets.UTF_8);
        return oldValue;
    }

    public void updateYamlInPath(String consulKVPath, String prefix, Object value) {
        // TODO  更新consulKVPath的以prefix前缀开始的部分配置，非更新所有配置
        // 读取kv原始值
        String oldValue = consulClient.getKVValue(consulKVPath).getValue().getValue();
        oldValue = oldValue == null ? "" : new String(Base64.getDecoder().decode(oldValue), StandardCharsets.UTF_8);
        log.info("原始配置：\n" + oldValue);

        // 读取要更新的配置key
        Yaml yaml = new Yaml();

        // 将所有yaml转为map
        LinkedHashMap rootMap = oldValue.equals("") ? new LinkedHashMap() : yaml.loadAs(oldValue, LinkedHashMap.class);

        // 修改要更新的key值
        String config = yaml.dumpAs(value, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
        LinkedHashMap updateMap = yaml.loadAs(config, LinkedHashMap.class);
        rootMap.put(prefix, updateMap);

        // 导出整体yaml
        String configRoot = yaml.dumpAs(rootMap, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
        log.info("修改配置：\n" + configRoot);

        // 写入整体yaml
        consulClient.setKVValue(consulKVPath, configRoot);
    }

    public String getPrefixByAnnotationConfigurationProperties(Object obj) {
        if (obj.getClass().isAnnotationPresent(ConfigurationProperties.class)) {
            ConfigurationProperties annotation = obj.getClass().getAnnotation(ConfigurationProperties.class);
            String prefix = annotation.prefix();
            return prefix;
        } else {
            return null;
        }
    }
}
