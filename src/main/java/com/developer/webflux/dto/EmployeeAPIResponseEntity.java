package com.developer.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 06/05/2021 - 11:36 PM
 * @createdBy duongtrong
 * @since 06/05/2021
 **/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAPIResponseEntity<T> {
    private int code;
    private String message;
    private T data;
}
