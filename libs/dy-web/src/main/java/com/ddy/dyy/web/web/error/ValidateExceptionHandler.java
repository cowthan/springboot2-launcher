package com.ddy.dyy.web.web.error;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.ddy.dyy.web.models.Response;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Integer.MIN_VALUE) // 优先级必须高于 DyWebExceptionHandler
public class ValidateExceptionHandler {

    public ValidateExceptionHandler() {
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public Response handleMethodArgumentNotValidException(BindException ex) {
        BindingResult bindingResult = ((BindException) ex).getBindingResult();
        StringBuilder sb = new StringBuilder("");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return Response.error(400, msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public Response handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder sb = new StringBuilder("");
        for (ConstraintViolation violation : violations) {
            sb.append(violation.getMessage());
        }
        String msg = sb.toString();
        return Response.error(400, msg);
    }

}
