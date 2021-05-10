package com.developer.webflux.controller;

import com.developer.webflux.dto.EmployeeDTO;
import com.developer.webflux.service.EmployeeService;
import com.developer.webflux.util.AbstractEndpoint;
import com.developer.webflux.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 06/05/2021 - 11:24 PM
 * @createdBy duongtrong
 * @since 06/05/2021
 **/

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController extends AbstractEndpoint {

    private final EmployeeService employeeService;

    @GetMapping
    public Mono<ResponseEntity<Object>> getAllEmployees() {
        return employeeService.getAllEmployees().collectList().map(ResponseEntityUtil::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createEmployee(@Validated @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO).map(ResponseEntityUtil::created);
    }
}
