package com.developer.webflux.repository;

import com.developer.webflux.model.Employee;
import org.springframework.data.repository.reactive.RxJava2SortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRxJavaRepository extends RxJava2SortingRepository<Employee, String> {
}
