package com.developer.webflux.controller;

import com.developer.webflux.dto.EmployeeDto;
import com.developer.webflux.service.EmployeeService;
import com.developer.webflux.util.AbstractEndpoint;
import com.developer.webflux.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController extends AbstractEndpoint {

    private final EmployeeService employeeService;

    @GetMapping
    public Mono<ResponseEntity<Object>> getAllEmployees() {
        log.info("Request get all employees.");
        return employeeService.getAllEmployees().collectList().map(ResponseEntityUtil::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createEmployee(@Validated @RequestBody Mono<EmployeeDto> employeeDTO) {
        log.info("Request created new object employee.");
        return employeeDTO.flatMap(employeeService::createEmployee).map(ResponseEntityUtil::created);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> updateEmployee(@PathVariable("id") String id, @Validated @RequestBody Mono<EmployeeDto> employeeDTO) {
        log.info("Request update object employee.");
        return employeeService.updateEmployee(id, employeeDTO).map(ResponseEntityUtil::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getEmployee(@PathVariable("id") String id) {
        log.info("Request get employee by ID: {}", id);
        return employeeService.getSingleEmployee(id).map(EmployeeDto::new).map(ResponseEntityUtil::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable("id") String id) {
        return employeeService.deleteEmployee(id).map(result -> ResponseEntity.noContent().build());
    }
}
