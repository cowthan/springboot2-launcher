package demo.mvc.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"demo.mvc"})
@EnableWebMvc //提供mvc支持
public class WebApplicationConfig implements WebMvcConfigurer {

    //设置响应信息编码集
    @Override
    public void extendMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) converters.get(1);
        stringHttpMessageConverter.setDefaultCharset(Charset.forName("utf-8"));
    }

    //提供静态资源的支持
    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {
        //客户端以/html/开始的请求，访问类路径下static/html
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}