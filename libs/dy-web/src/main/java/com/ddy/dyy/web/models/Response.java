package com.ddy.dyy.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response<T> {

    private int code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    protected Response() {
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response<>();
        resp.setData(data);
        resp.setCode(200);
        return resp;
    }

    public static Response<Void> ok() {
        Response<Void> resp = new Response<>();
        resp.setCode(200);
        return resp;
    }

    public static <T> ErrorResponse<T> error(int code, String label) {
        return new ErrorResponse<>(code, label);
    }

}
