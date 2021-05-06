package com.developer.webflux.dto;

import com.developer.webflux.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * webflux
 *
 * @author duongtrong
 * @version 1.0.0
 * @createdAt 06/05/2021 - 11:26 PM
 * @createdBy duongtrong
 * @since 06/05/2021
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    private String username;
    private String fullName;
    private String dateOfBirth;
    private int age;

    public EmployeeDTO(Employee employee) {
        this.username = employee.getUsername();
        this.fullName = employee.getFullName();
        this.dateOfBirth = employee.getDateOfBirth();
        this.age = employee.getAge();
    }
}
