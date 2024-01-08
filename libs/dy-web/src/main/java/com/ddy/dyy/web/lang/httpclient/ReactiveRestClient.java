package com.ddy.dyy.web.lang.httpclient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.ddy.dyy.web.lang.Lang;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

public class ReactiveRestClient {

    private final WebClient webClient;
    private final String baseUrl;
    private final int maxConnections = 50;

    public ReactiveRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        // 连接池
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(maxConnections)
                .pendingAcquireMaxCount(10_000)
                .maxIdleTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)));

        this.webClient = WebClient.builder()
                .baseUrl(this.baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public interface RestCallback {
        void onRequestFinished(boolean isSuccess, int statusCode, String body, Throwable e);
    }

    public void doPost(String uri, MediaType contentType, String body, RestCallback callback) {
        Mono<ResponseEntity<String>> mono = this.webClient
                .post()
                .uri(uri)
                .contentType(contentType)
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class);
        doSubscribe(mono, callback);
    }

    private void doSubscribe(Mono<ResponseEntity<String>> mono, RestCallback callback) {
        mono.subscribe(resp -> {
            callback.onRequestFinished(true, resp.getStatusCode().value(), resp.getBody(), null);
        }, throwable -> {
            if (throwable instanceof WebClientResponseException) {
                WebClientResponseException e = (WebClientResponseException) throwable;
                callback.onRequestFinished(false, e.getStatusCode().value(),
                        e.getResponseBodyAsString(StandardCharsets.UTF_8), throwable);
            } else {
                callback.onRequestFinished(false, -999, throwable.getMessage(), throwable);
            }
        });
    }

    public void doGet(String uri, RestCallback callback) {
        Mono<ResponseEntity<String>> mono = this.webClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);
        doSubscribe(mono, callback);
    }

    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.INFO);
        });

        RestCallback restCallback = new RestCallback() {
            @Override
            public void onRequestFinished(boolean isSuccess, int statusCode, String body, Throwable e) {
                if (!isSuccess) {
                    System.out.println("请求失败：" + statusCode + "--" + body);
                } else {
                    System.out.println("返回：" + body);
                }
            }
        };

        String baseUrl = "http://localhost:3333";
        ReactiveRestClient rc = new ReactiveRestClient(baseUrl);
        rc.doGet("/api/test/get1", restCallback);
        rc.doGet("/api/test/get2", restCallback);
        rc.doGet("/api/test/get3", restCallback);
        rc.doGet("/api/test/get500", restCallback);
        rc.doGet("/api/test/get404", restCallback);
        rc.doGet("/api/test/getTimeout", restCallback);

        Lang.sleep(4000000);
    }
}
