package com.ddy.dyy.web.lang.httpclient;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.ddy.dyy.web.lang.http.HttpEmitter;
import com.ddy.dyy.web.lang.http.impl.OkHttp3Worker;
import com.ddy.dyy.web.lang.Lang;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * rest client
 */
public final class RestClient {

    private RestClient() {
    }

    public static final int CODE_ERROR = 999;
    public static final int HTTP_OK_200 = 200;
    public static final int HTTP_OK_299 = 300;

    /**
     * error
     */
    public static class RestException extends RuntimeException {
        @Getter
        @Setter
        private int code;

        @Getter
        @Setter
        private String restMessage;

        public RestException(int code, String msg) {
            super(code + ": " + msg);
            this.code = code;
            this.restMessage = msg;
        }

        public RestException(int code, String msg, Throwable cause) {
            super(code + ": " + msg, cause);
            this.code = code;
            this.restMessage = msg;
        }
    }

    /**
     * @param responseEntity .
     * @return .
     */
    private static String processResult(ResponseEntity<byte[]> responseEntity) {
        if (responseEntity.getStatusCodeValue() >= HTTP_OK_200 && responseEntity.getStatusCodeValue() <= HTTP_OK_299) {
            return new String(responseEntity.getBody(), StandardCharsets.UTF_8);
        }
        throw new RestException(responseEntity.getStatusCodeValue(), responseEntity.hasBody()
                ? new String(responseEntity.getBody(), StandardCharsets.UTF_8) : "request fail");
    }

    /**
     * @param url .
     * @return .
     */
    public static String get(String url) {
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
            return processResult(responseEntity);
        } catch (Exception e) {
            if (e instanceof RestException) {
                throw e;
            } else if (e instanceof HttpStatusCodeException) {
                throw new RestException(((HttpStatusCodeException) e).getStatusCode().value(), e.getMessage(), e);
            } else {
                throw new RestException(CODE_ERROR, e.getMessage(), e);
            }
        }
    }

    private static RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
    }

    /**
     * .
     *
     * @param cookie .
     * @param url    .
     * @param json   .
     * @return .
     */
    public static String postJson(String cookie, String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        if (Lang.isNotEmpty(cookie)) {
            headers.add("Cookie", cookie);
        }
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(url, request, byte[].class);
            return processResult(responseEntity);
        } catch (Exception e) {
            if (e instanceof RestException) {
                throw e;
            } else if (e instanceof HttpStatusCodeException) {
                throw new RestException(((HttpStatusCodeException) e).getStatusCode().value(), e.getMessage(), e);
            } else {
                throw new RestException(CODE_ERROR, e.getMessage(), e);
            }
        }
    }

    public static void download(String url, File outFile) {
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
        System.out.println(responseEntity.getStatusCode());
        byte[] bytes = responseEntity.getBody();
        Lang.filePutContent(outFile.getAbsolutePath(), bytes);
    }

    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.INFO);
        });
//        String json = "{\n" +
////                "    \"path\":\"/1课程资源/H-情感专区/情感专区/【2020年以前情感综合】(7)/00 2016-2019年/10 老课程/006《奸.的好人》权.谋学/2、奸的好人系列\",\n" +
////                "    \"path\":\"/1课程资源/H-情感专区/情感专区/【2020年以前情感综合】(7)/00 2016-2019年/01 高端私教专区/熙德私教《后天蜕变，心态提升》\",\n" +
//                "    \"path\":\"/1课程资源/H-情感专区/情感专区/【SEX指导】(4)/【SVIP】『房事保肾』还精补脑法·初级/【SVIP】Nancy 罗南希（72式+……）/【VIP】罗南希《男性躯体科学延时》\",\n" +
//                "    \"password\":\"\",\"page_num\":1,\n" +
//                "    \"page_size\":30\n" +
//                "}";
//        String url = "https://xingxingziyuan.haohanba.cn/api/public/path";
//
//        AyoResponse ayoResponse = getHttpClient().fireSync(AyoRequest.request()
//                .actionPost()
//                .url(url)
//                .stringEntity(json));
//        System.out.println(JsonUtils.toJsonPretty(ayoResponse));
    }

    private static HttpEmitter getHttpClient() {
        HttpEmitter httpEmitter = new HttpEmitter();
        httpEmitter.worker(OkHttp3Worker.getDefault());
        return httpEmitter;
    }
}
