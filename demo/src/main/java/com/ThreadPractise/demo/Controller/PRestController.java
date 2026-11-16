package com.ThreadPractise.demo.Controller;


import com.ThreadPractise.demo.Model.Employee;
import com.ThreadPractise.demo.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/hit")
public class PRestController {

    private final EmployeeService employeeService;


    public PRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> hitThisThing(){
        return ResponseEntity.ok("This is Working");
    }



    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        log.info("create User");
        return employeeService.saveEmployee(employee);
    }


    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        log.info("get the users all users");
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        log.info("get only one user");
        return employeeService.getEmployeeById(id);
    }


    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        log.info("delete this user");
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully!";
    }

}