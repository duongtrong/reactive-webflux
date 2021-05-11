package com.developer.webflux.constant;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 07/05/2021 - 1:14 AM
 * @createdBy duongtrong
 * @since 07/05/2021
 **/

public enum ResponseEntityStatusConstant {
    
    SUCCESS(200, "Success"),
    CREATED(201, "Success"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request");
    
    private int code;
    private String message;

    ResponseEntityStatusConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
