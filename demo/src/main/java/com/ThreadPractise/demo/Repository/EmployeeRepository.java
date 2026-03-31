package com.ThreadPractise.demo.Repository;

import com.ThreadPractise.demo.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}