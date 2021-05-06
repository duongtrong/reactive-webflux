package com.developer.webflux.constant;

import lombok.Getter;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 07/05/2021 - 1:14 AM
 * @createdBy duongtrong
 * @since 07/05/2021
 **/

@Getter
public enum EmployeeConstant {
    
    SUCCESS(200, "Success");
    
    private int code;
    private String message;

    EmployeeConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
