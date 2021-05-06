package com.developer.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 06/05/2021 - 11:35 PM
 * @createdBy duongtrong
 * @since 06/05/2021
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails implements Serializable {
    private int status;
    private String message;
}
