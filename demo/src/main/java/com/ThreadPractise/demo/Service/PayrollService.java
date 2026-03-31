package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.Model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PayrollService {


    @Async("orderTaskExecutor")
    public CompletableFuture<String> createPayrollProfile(Employee employee){
        try {
            log.info("Payroll started for {} in thread {}",
                    employee.getEmployeeCode(), Thread.currentThread().getName());

            Thread.sleep(2000);

            log.info("Payroll completed for {}", employee.getEmployeeCode());
            return CompletableFuture.completedFuture("PAYROLL_CREATED");
        } catch (Exception e) {
            return payrollFallback(employee, e);
        }
    }

    public CompletableFuture<String> payrollFallback(Employee employee, Exception e){
        log.error("Payroll Fallback for employee {} due to {}", employee.getEmployeeCode(), e.getMessage());
        return CompletableFuture.completedFuture("PAYROLL_FAILED");
    }
}