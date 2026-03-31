package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.Model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class NotificationService {

    @Async("orderTaskExecutor")
    public CompletableFuture<String> sendWelcomeEmail(Employee employee) {
        try {
            log.info("Welcome email started for {} in thread {}",
                    employee.getEmail(), Thread.currentThread().getName());

            Thread.sleep(1000);

            if (employee.getEmail() != null && employee.getEmail().contains("fail")) {
                throw new RuntimeException("Email server unavailable");
            }

            log.info("Welcome email sent to {}", employee.getEmail());
            return CompletableFuture.completedFuture("WELCOME_EMAIL_SENT");
        } catch (Exception e) {
            return welcomeEmailFallback(employee, e);
        }
    }

    @Async("orderTaskExecutor")
    public CompletableFuture<String> notifyManager(Employee employee) {
        try {
            log.info("Manager notification started for {} in thread {}",
                    employee.getManagerEmail(), Thread.currentThread().getName());

            Thread.sleep(1200);

            log.info("Manager notified for employee {}", employee.getEmployeeCode());
            return CompletableFuture.completedFuture("MANAGER_NOTIFIED");
        } catch (Exception e) {
            return managerNotificationFallback(employee, e);
        }
    }

    public CompletableFuture<String> welcomeEmailFallback(Employee employee, Exception e) {
        log.error("Welcome email fallback for employee {} due to {}",
                employee.getEmployeeCode(), e.getMessage());
        return CompletableFuture.completedFuture("WELCOME_EMAIL_FAILED");
    }

    public CompletableFuture<String> managerNotificationFallback(Employee employee, Exception e) {
        log.error("Manager notification fallback for employee {} due to {}",
                employee.getEmployeeCode(), e.getMessage());
        return CompletableFuture.completedFuture("MANAGER_NOTIFY_FAILED");
    }
}