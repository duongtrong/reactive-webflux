package com.developer.webflux.service;

import com.developer.webflux.dto.EmployeeDto;
import com.developer.webflux.exception.CustomException;
import com.developer.webflux.model.Employee;
import com.developer.webflux.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .map(EmployeeDto::new);
    }

    @Override
    public Mono<Employee> getSingleEmployee(String id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("EmployeeID not found")));
    }

    @Override
    public Mono<EmployeeDto> createEmployee(EmployeeDto employeeDTO) {
        return Mono.fromCallable(() -> Employee.builder()
                .username(employeeDTO.getUsername())
                .fullName(employeeDTO.getFullName())
                .dateOfBirth(employeeDTO.getDateOfBirth())
                .age(employeeDTO.getAge())
                .build())
                .flatMap(x -> employeeRepository.existsByUsername(x.getUsername())
                        .flatMap(exist -> {
                                    if (Boolean.TRUE.equals(exist)) {
                                        return Mono.error(new CustomException("Username already exist!"));
                                    }
                                    return employeeRepository.save(x)
                                            .map(EmployeeDto::new);
                                }
                        )
                );
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDTO) {
        return this.getSingleEmployee(employeeDTO.getId())
                .flatMap(e -> {
                    e.setFullName(employeeDTO.getFullName());
                    e.setUsername(employeeDTO.getUsername());
                    e.setDateOfBirth(employeeDTO.getDateOfBirth());
                    e.setAge(employeeDTO.getAge());
                    return employeeRepository.save(e);
                }).log().map(EmployeeDto::new);
    }
}
