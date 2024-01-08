package com.ddy.dyy.web.models;

/**
 * 业务异常
 */
public class LogicException extends RuntimeException {

    private int statusCode;

    public LogicException(int statusCode, String label) {
        super(label);
        this.statusCode = statusCode;
    }


    public LogicException(int statusCode, String label, Throwable cause) {
        super(label, cause);
        this.statusCode = statusCode;
    }


    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getLabel() {
        return super.getMessage();
    }
}
