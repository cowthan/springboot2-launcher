package com.danger.t7;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.danger.t7.controller.SwaggerProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类功能描述
 */
//
@Configuration
public class SwaggerV3Config {
    private final SwaggerProperties swaggerProperties;


    public SwaggerV3Config(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30) // v2 不同
//                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.danger.t7.controller")) // 设置扫描路径
//                .apis(RequestHandlerSelectors.withClassAnnotation(Tag.class)) // 设置扫描路径
//                .build();
//    }

//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30).pathMapping("/")
//
//
//                // 定义是否开启swagger，false为关闭，可以通过变量控制
//                .enable(swaggerProperties.getEnable())
//
//
//                // 将api的元信息设置为包含在json ResourceListing响应中。
//                .apiInfo(apiInfo())
//
//
//                // 接口调试地址
//                .host(swaggerProperties.getTryHost())
//
//
//                // 选择哪些接口作为swagger的doc发布
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//
//
//                // 支持的通讯协议集合
//                .protocols(newHashSet("https", "http"));
//
//
//                // 授权信息设置，必要的header token等认证信息
////                .securitySchemes(securitySchemes())
//
//
//                // 授权信息全局应用
////                .securityContexts(securityContexts());
//    }

    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }

    /**
     * API 页面上半部分展示信息
     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title(swaggerProperties.getApplicationName() + " Api Doc")
//                .description(swaggerProperties.getApplicationDescription())
//                .contact(new Contact("lighter", null, "123456@gmail.com"))
//                .version("Application Version: " + swaggerProperties.getApplicationVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion())
//                .build();
//
//
//    }

}
