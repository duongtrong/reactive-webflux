package com.developer.webflux.service;

import com.developer.webflux.dto.EmployeeDTO;
import com.developer.webflux.exception.CustomException;
import com.developer.webflux.model.Employee;
import com.developer.webflux.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
    public Flux<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .map(EmployeeDTO::new);
    }

    @Override
    public Mono<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) {
        return Mono.fromCallable(
                () -> Employee.builder()
                        .username(employeeDTO.getUsername())
                        .fullName(employeeDTO.getFullName())
                        .dateOfBirth(employeeDTO.getDateOfBirth())
                        .age(employeeDTO.getAge())
                        .build())
                .flatMap(
                        x -> employeeRepository.existsByUsername(x.getUsername()).log()
                                .flatMap(
                                        exist -> {
                                            if (Boolean.TRUE.equals(exist)) {
                                                return Mono.error(new CustomException("Username already exist!"));
                                            }
                                            return employeeRepository.save(x)
                                                    .map(EmployeeDTO::new);
                                        }
                                )
                );
    }
}
