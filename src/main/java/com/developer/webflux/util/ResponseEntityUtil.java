package com.developer.webflux.util;

import com.developer.webflux.constant.ResponseEntityStatusConstant;
import com.developer.webflux.dto.EmployeeAPIResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    private ResponseEntityUtil() {
    }

    public static ResponseEntity<Object> ok(Object data) {
        EmployeeAPIResponseEntity<Object> responseEntity = EmployeeAPIResponseEntity.builder()
                .code(ResponseEntityStatusConstant.SUCCESS.getCode())
                .message(ResponseEntityStatusConstant.SUCCESS.getMessage())
                .data(data)
                .build();
        return ResponseEntity.ok(responseEntity);
    }

    public static ResponseEntity<Object> created(Object data) {
        EmployeeAPIResponseEntity<Object> responseEntity = EmployeeAPIResponseEntity.builder()
                .code(ResponseEntityStatusConstant.CREATED.getCode())
                .message(ResponseEntityStatusConstant.CREATED.getMessage())
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseEntity);
    }
}
