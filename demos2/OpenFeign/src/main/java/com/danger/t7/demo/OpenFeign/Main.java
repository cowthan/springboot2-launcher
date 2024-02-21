package com.danger.t7.demo.OpenFeign;

import java.util.concurrent.TimeUnit;

import com.danger.t7.demo.OpenFeign.annotation.SpringMvcContract;
import com.danger.t7.demo.OpenFeign.demo.api.MyApi;
import com.danger.t7.demo.OpenFeign.demo.github.GitHubClientError;
import com.danger.t7.demo.OpenFeign.demo.github.GitHubErrorDecoder;
import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.web.WebContext;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 */
@Slf4j
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext application = SpringApplication.run(Main.class, args);
        WebContext.printServerInfo(application);

        MyApi myApi = createFeignClient();
        System.out.println("用Feign请求个接口看看：");
        try {
            System.out.println("结果 - " + JsonUtils.toJson(myApi.test1()));
        } catch (GitHubClientError e) {
            System.out.println("出错了-" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static MyApi createFeignClient() {
        final Decoder decoder = new GsonDecoder();
        final Encoder encoder = new GsonEncoder();
        MyApi myApi = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(new GitHubErrorDecoder(decoder))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .contract(new SpringMvcContract())
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
//                .retryer()
//                .addCapability()
//                .exceptionPropagationPolicy()
//                .errorDecoder()
//                .requestInterceptor()
                .client(feignClient())
                .target(MyApi.class, "http://localhost:9494");
        return myApi;
    }

    public static Client feignClient(org.springframework.cloud.client.loadbalancer.LoadBalancerClient loadBalancerClient,
                                     LoadBalancerProperties properties,
                                     LoadBalancerClientFactory loadBalancerClientFactory) {
        ConnectionPool cp = new ConnectionPool(1000, 20, TimeUnit.SECONDS);

        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        okhttp3.OkHttpClient okHttpClient = builder.connectionPool(cp).build();

        okHttpClient.dispatcher().setMaxRequestsPerHost(64);
        return new FeignBlockingLoadBalancerClient(
                new OkHttpClient(okHttpClient), loadBalancerClient, properties,
                loadBalancerClientFactory);
    }

    public static Client feignClient() {
        ConnectionPool cp = new ConnectionPool(1000, 20, TimeUnit.SECONDS);

        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        okhttp3.OkHttpClient okHttpClient = builder.connectionPool(cp).build();

        okHttpClient.dispatcher().setMaxRequestsPerHost(64);
        Client ret = new OkHttpClient(okHttpClient);
        return ret;
    }
    /*
    springcloud open-feign:
    - FeignClientFactoryBean：configureFeign
     */
}
