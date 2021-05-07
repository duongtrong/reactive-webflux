package com.developer.webflux.controller;

import com.developer.webflux.dto.EmployeeDTO;
import com.developer.webflux.exception.CustomException;
import com.developer.webflux.service.EmployeeService;
import com.developer.webflux.util.AbstractEndpoint;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;

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
    public DeferredResult<ResponseEntity<Object>> getAllEmployees() {
        return toDeferredResult(
                new DeferredResult<>(),
                employeeService.getAllEmployees());
    }

    @PostMapping
    public DeferredResult<ResponseEntity<Object>> createEmployee(@Validated @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        return toDeferredResult(
                new DeferredResult<>(),
                toObservable(bindingResult)
                        .flatMap(v -> employeeService.createEmployee(employeeDTO))
                        .subscribeOn(Schedulers.boundedElastic()),
                bindingResult);
    }
}
