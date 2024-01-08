package com.ddy.dyy.web.web.response;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import com.ddy.dyy.web.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;


/**
 * 统一接口返回规格
 */
@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DyRestResponseWrapAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private AtomicInteger responseCount = new AtomicInteger(0);

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
                                           MediaType contentType,
                                           MethodParameter returnType,
                                           ServerHttpRequest request,
                                           ServerHttpResponse response) {
        Method method = returnType.getMethod();
        if (method != null && MediaType.APPLICATION_JSON.equals(contentType)) {
            Object returnValue = bodyContainer.getValue();
            if (method.getReturnType() == ResponseEntity.class) {
                log.info("接口返回ResponseEntity，不进行任何修改");
                return;
            }
            if (returnValue instanceof Response) {
                response.setStatusCode(HttpStatus.valueOf(((Response) returnValue).getCode()));
            } else {
                bodyContainer.setValue(Response.ok(returnValue));
            }
            response.getHeaders().add("x-response-sequence", responseCount.incrementAndGet() + "");
        }
    }

}
