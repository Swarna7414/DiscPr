package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.DTO.EmployeeRequest;
import com.ThreadPractise.demo.Model.Employee;
import com.ThreadPractise.demo.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeOnboardingService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeBackgroundService employeeBackgroundService;

    private final EmployeeWorkflowService employeeWorkflowService;
    @Transactional
    public Long onBoardEmployee(EmployeeRequest employeeRequest){
        Employee employee = new Employee();
        employee.setEmployeeCode(employeeRequest.getEmployeeCode());
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhone(employeeRequest.getPhone());
        employee.setDepartment(employeeRequest.getDepartment());
        employee.setDesignation(employeeRequest.getDesignation());
        employee.setSalary(employeeRequest.getSalary());
        employee.setManagerEmail(employeeRequest.getManagerEmail());
        employee.setAddress(employeeRequest.getAddress());
        employee.setDateOfJoining(employeeRequest.getDateOfJoining());
        employee.setStatus("CREATED");

        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee saved with ID {}", savedEmployee.getId());

        employeeWorkflowService.startOnboarding(savedEmployee.getId());

        return savedEmployee.getId();
    }
}