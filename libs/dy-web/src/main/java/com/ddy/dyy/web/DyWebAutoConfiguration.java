package com.ddy.dyy.web;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ddy.dyy.web.DyWebProperties;
import com.ddy.dyy.web.web.logging.DyLoggingFilter;
import com.ddy.dyy.web.web.logging.DyLoggingInterceptor;
import com.ddy.dyy.web.web.logging.DyRepeatableFilter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AutoConfigureBefore({WebMvcAutoConfiguration.class})
public class DyWebAutoConfiguration {
    @Bean
    public DyWebProperties commonWebProperties() {
        return new DyWebProperties();
    }


    @Bean
    public WebMvcConfigurer dyWebMvcConfigurer(DyWebProperties webProperties) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new DyLoggingInterceptor(webProperties));
            }
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                // response json设置
                /*
                spring.jackson.date-format=yyyy-MM-dd HH:mm:ss不生效，Date还是返回毫秒，用这个配置解决
使用此方法, 以下 spring-boot: jackson时间格式化 配置 将会失效 spring.jackson.time-zone=GMT+8 spring.jackson.date-format=yyyy-MM-dd HH:mm:ss 原因: 会覆盖 @EnableAutoConfiguration 关于 WebMvcAutoConfiguration 的配置
                 */

                for (int i = 0; i < converters.size(); i++) {
                    if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                        MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) converters.get(i);
                        converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                converter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        // 控制Date字段的返回格式
                        if ("timestamps".equals(webProperties.getResponseTimeFormat())) {
                            // 毫秒时间戳
                            converter.getObjectMapper().configure(
                                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                        } else if ("ISO-8601".equals(webProperties.getResponseTimeFormat())) {
                            converter.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            converter.getObjectMapper().setDateFormat(new StdDateFormat().withColonInTimeZone(true));
                        } else {
                            converter.getObjectMapper().setDateFormat(
                                    new SimpleDateFormat(webProperties.getResponseTimeFormat()));
                        }
                        if (webProperties.isResponseLongToString()) {
                            //long转string
                            SimpleModule simpleModule = new SimpleModule();
                            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                            converter.getObjectMapper().registerModule(simpleModule);
                        }
                    }
                }
            }
        };
    }

    /**
     * .
     *
     * @return .
     */
    @Bean(name = "dy-repeatableFilter-registration-bean")
    public FilterRegistrationBean<DyRepeatableFilter> repeatableFilterRegisterBean() {
        FilterRegistrationBean<DyRepeatableFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new DyRepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("dyRepeatableFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * @param webProperties .
     * @return .
     */
    @Bean(name = "dy-loggingFilter-registration-bean")
    public FilterRegistrationBean<DyLoggingFilter> loggingFilterRegisterBean(DyWebProperties webProperties) {
        FilterRegistrationBean<DyLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new DyLoggingFilter(webProperties));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("dyLoggingFilter");
        filterRegistrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        return filterRegistrationBean;
    }
}
