package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.Model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class IdCardService {

    @Async("orderTaskExecutor")
    public CompletableFuture<String> createIdCardRequest(Employee employee) {
        try {
            log.info("ID card request started for {} in thread {}",
                    employee.getEmployeeCode(), Thread.currentThread().getName());

            Thread.sleep(1500);

            log.info("ID card request created for {}", employee.getEmployeeCode());
            return CompletableFuture.completedFuture("ID_CARD_CREATED");
        } catch (Exception e) {
            return idCardFallback(employee, e);
        }
    }

    public CompletableFuture<String> idCardFallback(Employee employee, Exception e) {
        log.error("ID card fallback for employee {} due to {}",
                employee.getEmployeeCode(), e.getMessage());
        return CompletableFuture.completedFuture("ID_CARD_FAILED");
    }
}