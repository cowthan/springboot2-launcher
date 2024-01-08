package com.ddy.dyy.web.web.error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddy.dyy.web.models.ErrorResponse;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.models.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * 接口异常统一拦截
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "dy.config.web", name = "rest-controller-error-advice-enable", matchIfMissing = true)
public class DyWebExceptionHandler
        extends ResponseEntityExceptionHandler
        implements PriorityOrdered, Filter {

    private final ObjectMapper objectMapper;


    ResponseEntityExceptionHandler exceptionHandler = new ResponseEntityExceptionHandler() {
    };
//    @ExceptionHandler(LogicException.class)
//    public AnyResponse onBizException(LogicException bizException,
//                                         HttpServletRequest request) {
//        // 将异常包装为AnyResponse
//        AnyErrorResponse ret = AnyResponse.error(bizException.getStatusCode(), bizException.getLabel());
//        return ret;
//    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        log.error("Web请求不规范: ", ex);
        ErrorResponse<?> ret = Response.error(status.value(), ExceptionUtils.getMessage(ex));
        return ResponseEntity.status(status.value()).body(ret);
    }

    @ExceptionHandler(Exception.class)
    public Response onException(Exception exception, HttpServletRequest request) {
        log.error("Web请求异常[02]: ", exception);
        ErrorResponse ret = null;
        if (exception instanceof LogicException) {
            LogicException e = (LogicException) exception;
            ret = Response.error(e.getStatusCode(), e.getLabel());
        } else {
            ret = Response.error(500, ExceptionUtils.getMessage(exception));
        }
        return ret;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            handleFilterBizException(e, servletRequest, servletResponse);
        }
    }


    /**
     * filter里抛出的异常处理
     */
    private void handleFilterBizException(Exception exception, ServletRequest servletRequest, ServletResponse servletResponse) {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            Response<?> errorResponse = onException(exception, request);
            response.setStatus(errorResponse.getCode());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            log.error("filter error: ", e);
        }
    }
}
