package com.ddy.dyy.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


public class ErrorResponse<T> extends Response<T> {

    public ErrorResponse() {
    }

    public ErrorResponse(int code, String label) {
        super();
        this.setCode(code);
        this.setMessage(label);
    }


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;


}
