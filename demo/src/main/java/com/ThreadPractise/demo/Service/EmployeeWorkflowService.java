package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.Model.Employee;
import com.ThreadPractise.demo.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeWorkflowService {

    private final EmployeeRepository employeeRepository;
    private final PayrollService payrollService;
    private final ITAccountService itAccountService;
    private final IdCardService idCardService;
    private final NotificationService notificationService;


    @Async("orderTaskExecutor")
    public void startOnboarding(Long employeeId){

        log.info("Starting onboarding workflow for employeeId {} in thread {}", employeeId, Thread.currentThread().getName());
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setStatus("PROCESSING");
        employeeRepository.save(employee);


        CompletableFuture<String> payrollfuture =  payrollService.createPayrollProfile(employee);
        CompletableFuture<String> itFuture = itAccountService.createItAccount(employee);
        CompletableFuture<String> idCardFuture = idCardService.createIdCardRequest(employee);
        CompletableFuture<String> welcomeMailFuture = notificationService.sendWelcomeEmail(employee);
        CompletableFuture<String> managerNotifyFuture = notificationService.notifyManager(employee);

        CompletableFuture.allOf(
                payrollfuture,
                itFuture,
                idCardFuture,
                welcomeMailFuture,
                managerNotifyFuture
        ).join();

        employee.setStatus("COMPLETED");
        employeeRepository.save(employee);

        log.info("Completed onboarding workflow for employeeId {}", employeeId);

    }

}