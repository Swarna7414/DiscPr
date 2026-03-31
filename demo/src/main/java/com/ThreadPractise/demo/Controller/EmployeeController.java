package com.ThreadPractise.demo.Controller;


import com.ThreadPractise.demo.DTO.EmployeeRequest;
import com.ThreadPractise.demo.Service.EmployeeOnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeOnboardingService employeeOnboardingService;

    @PostMapping("/onboard")
    public ResponseEntity<Map<String, Object>> onboardEmployee(@RequestBody EmployeeRequest request) {
        Long employeeId = employeeOnboardingService.onBoardEmployee(request);

        return ResponseEntity.ok(Map.of(
                "message", "Employee onboarding started successfully",
                "employeeId", employeeId
        ));
    }
}