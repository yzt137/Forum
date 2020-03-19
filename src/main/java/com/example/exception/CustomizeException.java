package com.example.exception;

public class CustomizeException  extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
