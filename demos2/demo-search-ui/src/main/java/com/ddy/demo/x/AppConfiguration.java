package com.ddy.demo.x;

import java.util.List;

import com.ddy.dyy.web.lang.Lang;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MyCorsConfiguration
 */
@Configuration(proxyBeanMethods = false)
public class AppConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                List<String> headers = Lang.newArrayList();
                headers.add("x-user-agent");
                headers.add("x-app-id");
                headers.add("x-danger-token");
                headers.add("authorization");
                headers.add("Origin");
                headers.add("X-Requested-With");
                headers.add("Content-Type");
                headers.add("Accept");
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                        .maxAge(3600)
                        .allowedHeaders(headers.toArray(new String[headers.size()]))
                        .allowCredentials(true);
            }
        };
    }

    /**
     * 防止出现：https://blog.csdn.net/weixin_42753193/article/details/127739370
     *
     * @return .
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers((TomcatContextCustomizer) context -> {
            context.setCookieProcessor(new LegacyCookieProcessor());
        });
    }

}