package com.developer.webflux.service;

import com.developer.webflux.dto.EmployeeDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 06/05/2021 - 11:25 PM
 * @createdBy duongtrong
 * @since 06/05/2021
 **/
public interface EmployeeService {

    Flux<EmployeeDTO> getAllEmployees();

    Mono<EmployeeDTO> createEmployee(EmployeeDTO employee);
}
