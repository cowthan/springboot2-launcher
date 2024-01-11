package com.ddy.dyy.web.web.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddy.dyy.web.DyWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class DyLoggingInterceptor implements HandlerInterceptor {
    private final DyWebProperties webProperties;
    private final LogPrinter logPrinter;
    //    long start = 0;
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    public DyLoggingInterceptor(DyWebProperties webProperties) {
        this.webProperties = webProperties;
        this.logPrinter = new LogPrinter(webProperties, log);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logPrinter.beforeRequest();
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // 这里可以进行一些后处理操作，例如数据转换等

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        try {
            if (webProperties.getLogMode() != null && webProperties.getLogMode().contains("interceptor")) {
                logPrinter.doPrint(request, response, startTime.get());
            }
        } catch (Exception e) {
            // ignore
        }
        startTime.remove();

    }
}
