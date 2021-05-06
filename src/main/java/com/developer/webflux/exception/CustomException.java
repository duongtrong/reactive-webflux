package com.developer.webflux.exception;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 07/05/2021 - 1:13 AM
 * @createdBy duongtrong
 * @since 07/05/2021
 **/
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
