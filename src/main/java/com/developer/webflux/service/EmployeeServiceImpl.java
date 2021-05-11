package com.developer.webflux.service;

import com.developer.webflux.constant.Status;
import com.developer.webflux.dto.EmployeeDto;
import com.developer.webflux.exception.CustomException;
import com.developer.webflux.model.Employee;
import com.developer.webflux.repository.EmployeeRepository;
import com.developer.webflux.repository.EmployeeRxJavaRepository;
import io.reactivex.Completable;
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

    private final EmployeeRxJavaRepository employeeRxJavaRepository;

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
                .status(Status.ACTIVE.getValue())
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
    public Mono<EmployeeDto> updateEmployee(final String id, Mono<EmployeeDto> employeeDTO) {
        return this.getSingleEmployee(id)
                .flatMap(e -> employeeDTO.map(u -> {
                    e.setFullName(u.getFullName());
                    e.setUsername(u.getUsername());
                    e.setDateOfBirth(u.getDateOfBirth());
                    e.setAge(u.getAge());
                    return e;
                }).flatMap(employeeRepository::save)
                        .log().map(EmployeeDto::new));
    }

    @Override
    public Mono<Void> deleteEmployee(String id) {
        return employeeRepository.findById(id).flatMap(employeeRepository::delete);
    }

    @Override
    public Completable deleteEmployees(String id) {
        return Completable.fromCallable(() -> employeeRxJavaRepository.deleteById(id));
    }
}
