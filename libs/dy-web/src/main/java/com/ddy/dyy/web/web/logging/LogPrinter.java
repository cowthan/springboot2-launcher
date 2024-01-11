package com.ddy.dyy.web.web.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddy.dyy.web.DyWebProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class LogPrinter {
    public static final MediaType RPC_MEDIA_TYPE = new MediaType("application", "rpc-json");
    private static final String REQUEST_ID = "msg_id";

    private final DyWebProperties webProperties;
    private Logger log;

    public LogPrinter(DyWebProperties webProperties, Logger log) {
        this.webProperties = webProperties;
        this.log = log;
    }

    public void beforeRequest(){
        String requestId = "request_" + UUID.randomUUID().toString().replace("-", "");
        MDC.put(REQUEST_ID, requestId);
    }

    public void doPrint(HttpServletRequest request,
                        HttpServletResponse response, long start) throws IOException {
        if (!needLog(request)) {
            return;
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }

        try {
            DyLoggingFilter.LoggingRequest loggingRequest = getLoggingRequest(request);
            copyBodyToResponse(response);
            long responseTimeMs = System.currentTimeMillis() - start;
            DyLoggingFilter.LoggingResponse loggingResponse =
                    getLoggingResponse(response, ((ContentCachingResponseWrapper) response).getContentAsByteArray());
            String responseError = "code = " + loggingResponse.getCode();
            if (loggingResponse.getCode() != HttpStatus.OK.value()) {
                responseError += "，content = " + loggingResponse.getBody();
            }
            log("After Request[{}] from [{}]， cost {}ms，params = [{}], request body = [{}], response = [{}]",
                    request.getRequestURI(), getIp(request), responseTimeMs, loggingRequest.getParams(),
                    loggingRequest.getBody() == null ? "" : loggingRequest.getBody(), responseError);
        } catch (Exception e) {
            log.error("请求日志打印出错", e);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }

    /**
     * .
     *
     * @param format    .
     * @param arguments .
     */
    private void log(String format, Object... arguments) {
        try {
            if (webProperties.getLogLevel().equalsIgnoreCase("debug")) {
                log.debug(format, arguments);
            } else if (webProperties.getLogLevel().equalsIgnoreCase("warn")) {
                log.warn(format, arguments);
            } else {
                log.info(format, arguments);
            }
        } catch (Exception e) {
            log.error("log error", e);
        }
    }

    /**
     * .
     *
     * @param request .
     * @return .
     */
    private boolean needLog(HttpServletRequest request) {
        if (webProperties.isLogEnable() && !StringUtils.isEmpty(
                webProperties.getLogIgnoreKeywords().trim())) {
            String[] words = webProperties.getLogIgnoreKeywords().trim().split(",");
            for (String word : words) {
                if (request.getRequestURI().contains(word.trim())) {
                    return false;
                }
            }
        }
        return webProperties.isLogEnable();
    }

    /**
     * .
     *
     * @param request .
     * @return .
     */
    private  DyLoggingFilter.LoggingRequest getLoggingRequest(HttpServletRequest request) {

        Map<String, String> headers = getHeaders(request);
        DyLoggingFilter.LoggingRequest loggingRequest = new  DyLoggingFilter.LoggingRequest();
        loggingRequest.setMethod(request.getMethod());
        loggingRequest.setPath(request.getRequestURI());
        loggingRequest.setParams(getParameters(request.getParameterMap()));
        loggingRequest.setHeaders(headers);

        if (request.getMethod().equalsIgnoreCase("post")
                || request.getMethod().equalsIgnoreCase("put")) {
            if ((request instanceof HttpServletRequest)
                    && (StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)
                    || StringUtils.startsWithIgnoreCase(request.getContentType(), RPC_MEDIA_TYPE.toString()))) {
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    loggingRequest.setBody(sb.toString());
                    reader.close();
                } catch (Throwable e) {
                    log.error("error", e);
                }
            }
        }

        return loggingRequest;
    }

    /**
     * @param response .
     * @param body     .
     * @return .
     * @throws IOException .
     */
    private  DyLoggingFilter.LoggingResponse getLoggingResponse(HttpServletResponse response, byte[] body) throws IOException {

        Map<String, String> headers = getHeaders(response);
        ContentCachingResponseWrapper wrapper = WebUtils
                .getNativeResponse(response, ContentCachingResponseWrapper.class);

        DyLoggingFilter.LoggingResponse loggingResponse = new  DyLoggingFilter.LoggingResponse();
        loggingResponse.setHeaders(headers);
        loggingResponse.setCode(response.getStatus());

        String responseBody = new String(body,
                wrapper.getCharacterEncoding());
        loggingResponse.setBody(responseBody);
        return loggingResponse;
    }

    /**
     * .
     *
     * @param response .
     * @throws IOException .
     */
    private void copyBodyToResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils
                .getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    /**
     * .
     *
     * @param parameterMap .
     * @return .
     */
    public Map<String, String> getParameters(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
            String[] values = e.getValue();
            return values.length > 0 ? String.join(",", values) : "[EMPTY]";
        }));
    }

    /**
     * .
     *
     * @param request .
     * @return .
     */
    public Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>(0);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName != null) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }

    /**
     * .
     *
     * @param response .
     * @return .
     */
    public Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>(0);
        for (String headerName : response.getHeaderNames()) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = "未知客户端";
        }
        return ip;
    }
}
