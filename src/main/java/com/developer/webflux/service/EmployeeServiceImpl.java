package com.developer.webflux.service;

import com.developer.webflux.dto.EmployeeDTO;
import com.developer.webflux.exception.CustomException;
import com.developer.webflux.model.Employee;
import com.developer.webflux.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<List<EmployeeDTO>> getAllEmployees() {
        return employeeRepository.findAll()
                .log()
                .doOnNext(employee -> log.info(employee.toString()))
                .publishOn(Schedulers.boundedElastic())
                .map(EmployeeDTO::new).buffer();
    }

    @Override
    public Mono<Employee> createEmployee(EmployeeDTO employeeDTO) {
        return Mono.fromCallable(
                () -> Employee.builder()
                        .username(employeeDTO.getUsername())
                        .fullName(employeeDTO.getFullName())
                        .dateOfBirth(employeeDTO.getDateOfBirth())
                        .age(employeeDTO.getAge())
                        .build())
                .flatMap(employeeRepository::save);
    }

    @Override
    public Mono<Employee> isExistUsername(String username) {
        return Mono.fromCallable(() -> employeeRepository.existsByUsername(username).orElseThrow(() -> new CustomException("Not found")));
    }
}
