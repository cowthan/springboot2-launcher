package com.ddy.dyy.web.uc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对接口进行分类，可按分类添加特定前缀，以满足不同项目需求
 * @Configuration
 * public class ProxyWebMvcConfig implements WebMvcConfigurer {
 *
 *     @Override
 *     public void configurePathMatch(PathMatchConfigurer configurer) {
 *         //如果controller上有OfficeRestController注解，就替换path路径
 *         configurer.addPathPrefix("/nats", c -> c.isAnnotationPresent(OfficeRestController.class));
 *     }
 *
 * }
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface UserCenterRestController {
    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};
}
