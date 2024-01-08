package com.ddy.dyy.mybatis.sqlrunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class DataSource2 {

    private static ConcurrentHashMap<String, SqlSessionFactory> sqlSessionFactories = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, PooledDataSource> dataSources = new ConcurrentHashMap<>();


    public static PooledDataSource getDataSource(DataSourceConfig config) {
        String key = config.toString();
        return dataSources.computeIfAbsent(key, k -> {
            String url = null;

            if ("mysql".equals(config.getDbtype())) {
                url = "jdbc:mysql://${dbHost}:${dbPort}/${db}" +
                        "?useUnicode=true" +
                        "&characterEncoding=UTF-8" +
                        "&allowPublicKeyRetrieval=True" +
                        "&allowMultiQueries=true" +
                        "&useSSL=false" +
                        "&serverTimezone=GMT%2B8";
            } else if ("postgres".equals(config.getDbtype())) {
                url = "jdbc:postgresql://${dbHost}:${dbPort}/${db}" +
                        "?useUnicode=true" +
                        "&characterEncoding=UTF-8" +
                        "&allowPublicKeyRetrieval=True" +
                        "&allowMultiQueries=true" +
                        "&useSSL=false" +
                        "&serverTimezone=GMT%2B8";
            }

            url = url.replace("${dbHost}", config.getHost());
            url = url.replace("${dbPort}", config.getPort() + "");
            url = url.replace("${db}", config.getDatabase());

            PooledDataSource dataSource = new PooledDataSource();

            Map<String, String> drivers = new HashMap<>();
            drivers.put("mysql", "com.mysql.cj.jdbc.Driver");
            drivers.put("postgres", "org.postgresql.Driver");

            dataSource.setDriver(drivers.get(config.getDbtype()));
            dataSource.setUrl(url);
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());
            return dataSource;
        });
    }


    public static SqlSession getSqlSession(DataSourceConfig config, List<Class> mappers) {
        String key = config.toString();
        SqlSessionFactory factory = sqlSessionFactories.computeIfAbsent(key, k -> {
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, getDataSource(config));
            Configuration configuration = new Configuration(environment);
            configuration.setMapUnderscoreToCamelCase(true);
            for (Class cls: mappers){
                configuration.addMapper(cls);
            }
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            return sqlSessionFactory;
        });
        return factory.openSession();
    }


    public static SqlSessionFactory getSqlSessionFactory(DataSource dataSource, ResourceLoader resourceLoader,
                                                      MybatisPlusProperties properties,
                                                      Interceptor[] interceptors,
                                                      DatabaseIdProvider databaseIdProvider,
                                                      TypeHandler[] typeHandlers,
                                                      Class[] mappers) throws Exception {
        // TODO 使用 MybatisSqlSessionFactoryBean 而不是 SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
//        if (StringUtils.hasText(properties.getConfigLocation())) {
//            factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
//        }

        // TODO 使用 MybatisConfiguration
//        MybatisConfiguration configuration = properties.getConfiguration();
//        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
//            configuration = new MybatisConfiguration();
//        }
//        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
//            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
//                customizer.customize(configuration);
//            }
//        }
        MybatisConfiguration configuration = new MybatisConfiguration();
        for (Class mapper : mappers) {
            configuration.addMapper(mapper);
        }
        factory.setConfiguration(configuration);

        if (properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(interceptors)) {
            factory.setPlugins(interceptors);
        }

        if (databaseIdProvider != null) {
            factory.setDatabaseIdProvider(databaseIdProvider);
        }


        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (properties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }

        if (!ObjectUtils.isEmpty(typeHandlers)) {
            factory.setTypeHandlers(typeHandlers);
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }
        // TODO 修改源码支持定义 TransactionFactory
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        factory.setTransactionFactory(transactionFactory);
//        this.getBeanThen(TransactionFactory.class, factory::setTransactionFactory);

        // TODO 对源码做了一定的修改(因为源码适配了老旧的mybatis版本,但我们不需要适配)
//        Class<? extends LanguageDriver> defaultLanguageDriver = properties.getDefaultScriptingLanguageDriver();
//        if (!ObjectUtils.isEmpty(this.languageDrivers)) {
//            factory.setScriptingLanguageDrivers(this.languageDrivers);
//        }
//        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);

//        applySqlSessionFactoryBeanCustomizers(factory);

        // TODO 此处必为非 NULL
        GlobalConfig globalConfig = properties.getGlobalConfig();
        // TODO 注入填充器
//        this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
//        // TODO 注入参与器
//        this.getBeanThen(PostInitTableInfoHandler.class, globalConfig::setPostInitTableInfoHandler);
//        // TODO 注入主键生成器
//        this.getBeansThen(IKeyGenerator.class, i -> globalConfig.getDbConfig().setKeyGenerators(i));
//        // TODO 注入sql注入器
//        this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
//        // TODO 注入ID生成器
//        this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
        // TODO 设置 GlobalConfig 到 MybatisSqlSessionFactoryBean
        factory.setGlobalConfig(globalConfig);
        return factory.getObject();
    }



    public static String getDbNameFromUrl(String url) {
        String[] parts = url.split("://");
        if (parts.length == 2) {
            String[] parts2 = parts[1].split("/");
            if (parts2.length > 1) {
                return parts2[1].split("\\?")[0];
            }
        }
        return null;
    }
}
