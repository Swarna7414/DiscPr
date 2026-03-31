package com.ThreadPractise.demo.DTO;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private Double salary;
    private String managerEmail;
    private String address;
    private String dateOfJoining;
}