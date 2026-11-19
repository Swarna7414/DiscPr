package com.ThreadPractise.demo.Controller;

import com.ThreadPractise.demo.Model.Employee;
import com.ThreadPractise.demo.Service.EmployeeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/checking")
    public ResponseEntity<String> hitThisService(){
        return ResponseEntity.ok("THis is Being hited");
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        return ResponseEntity.ok(
                employeeService.createEmployee(employee)
        );
    }


    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employee)
        );
    }


    @PostMapping(
            value = "/{id}/files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Employee> uploadFiles(
            @PathVariable Long id,
            @RequestParam(value = "image", required = false)
            MultipartFile image,
            @RequestParam(value = "file", required = false)
            MultipartFile file
    ) throws IOException {

        if ((image == null || image.isEmpty()) &&
                (file == null || file.isEmpty())) {
            throw new IllegalArgumentException(
                    "Please provide an image or document"
            );
        }

        return ResponseEntity.ok(
                employeeService.uploadFiles(id, image, file)
        );
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> downloadImage(
            @PathVariable Long id) {

        byte[] image = employeeService.downloadImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"employee-image\""
                )
                .body(image);
    }


    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long id) {

        byte[] file = employeeService.downloadDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"employee-document\""
                )
                .body(file);
    }


    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long id) {

        employeeService.deleteImage(id);

        return ResponseEntity.ok("Employee image deleted");
    }


    @DeleteMapping("/{id}/document")
    public ResponseEntity<String> deleteDocument(
            @PathVariable Long id) {

        employeeService.deleteDocument(id);

        return ResponseEntity.ok("Employee document deleted");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok("Employee deleted successfully");
    }
}