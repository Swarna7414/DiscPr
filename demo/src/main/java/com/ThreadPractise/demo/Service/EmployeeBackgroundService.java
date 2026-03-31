package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.Model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeBackgroundService {

    private final PayrollService payrollService;

    private final ITAccountService itAccountService;

    private final NotificationService notificationService;

    private final IdCardService idCardService;

    @Async("orderTaskExecutor")
    public void processEmployee(Employee savedEmployee) {
        log.info("Started onboarding workflow for employee {} in thread {}", savedEmployee.getEmployeeCode(),
                Thread.currentThread().getName());

        payrollService.createPayrollProfile(savedEmployee);
        itAccountService.createItAccount(savedEmployee);
        idCardService.createIdCardRequest(savedEmployee);
        notificationService.sendWelcomeEmail(savedEmployee);
        notificationService.notifyManager(savedEmployee);

        log.info("Completed onboarding workflow for employee {}", savedEmployee.getEmployeeCode());

    }
}