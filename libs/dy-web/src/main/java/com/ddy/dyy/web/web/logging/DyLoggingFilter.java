package com.ddy.dyy.web.web.logging;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddy.dyy.web.DyWebProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Order
public class DyLoggingFilter extends OncePerRequestFilter {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final DyWebProperties webProperties;
    private final LogPrinter logPrinter;

    public DyLoggingFilter(DyWebProperties webProperties) {
        this.webProperties = webProperties;
        this.logPrinter = new LogPrinter(webProperties, log);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        startTime.set(System.currentTimeMillis());
        filterChain.doFilter(request, response);
        try {
            if (webProperties.getLogMode() != null && webProperties.getLogMode().contains("filter")) {
                logPrinter.doPrint(request, response, startTime.get());
            }
        } catch (Exception e) {
            // ignore
        }

        startTime.remove();
    }

    @Getter
    @Setter
    public static class LoggingRequest {
        private String clientIp;
        private String method;
        private String path;
        private Map<String, String> params;
        private Map<String, String> headers;
        private String body;
    }

    @Getter
    @Setter
    public static class LoggingResponse {
        private Map<String, String> headers;
        private String body;
        private int code;
    }
}
