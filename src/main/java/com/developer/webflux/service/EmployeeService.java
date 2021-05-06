package com.developer.webflux.service;

import com.developer.webflux.dto.EmployeeDTO;
import com.developer.webflux.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    
    Flux<List<EmployeeDTO>> getAllEmployees();
    
    Mono<Employee> createEmployee(EmployeeDTO employee);
    
    Mono<Employee> isExistUsername(String fullName);
}
