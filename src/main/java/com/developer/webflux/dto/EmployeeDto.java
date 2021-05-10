package com.developer.webflux.dto;

import com.developer.webflux.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto implements Serializable {

    private String id;
    @NotNull(message = "username not be null")
    private String username;
    @NotNull(message = "fullName not be null")
    private String fullName;
    private String dateOfBirth;
    private int age;

    public EmployeeDto(Employee employee) {
        this.username = employee.getUsername();
        this.fullName = employee.getFullName();
        this.dateOfBirth = employee.getDateOfBirth();
        this.age = employee.getAge();
    }
}
