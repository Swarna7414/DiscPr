package com.ThreadPractise.demo.Service;


import com.ThreadPractise.demo.Model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class ITAccountService {

    @Async("orderTaskExecutor")
    public CompletableFuture<String> createItAccount(Employee employee) {
        try {
            log.info("IT account creation started for {} in thread {}",
                    employee.getEmployeeCode(), Thread.currentThread().getName());

            Thread.sleep(2500);

            log.info("IT account created for {}", employee.getEmployeeCode());
            return CompletableFuture.completedFuture("IT_ACCOUNT_CREATED");
        } catch (Exception e) {
            return itAccountFallback(employee, e);
        }
    }

    public CompletableFuture<String> itAccountFallback(Employee employee, Exception e) {
        log.error("IT account fallback for employee {} due to {}",
                employee.getEmployeeCode(), e.getMessage());
        return CompletableFuture.completedFuture("IT_ACCOUNT_FAILED");
    }
}