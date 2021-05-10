package com.developer.webflux.repository;

import com.developer.webflux.model.Employee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
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

@Repository
public interface EmployeeRepository extends R2dbcRepository<Employee, String> {

    Mono<Boolean> existsByUsername(String username);
}
