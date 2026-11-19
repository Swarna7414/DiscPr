package com.ThreadPractise.demo.Service;


import com.ThreadPractise.demo.Exception.EmployeeNotFoundException;
import com.ThreadPractise.demo.Model.Employee;
import com.ThreadPractise.demo.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final S3Service s3Service;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            S3Service s3Service
    ) {
        this.employeeRepository = employeeRepository;
        this.s3Service = s3Service;
    }


    public Employee createEmployee(Employee employee) {

        employee.setId(null);

        return employeeRepository.save(employee);
    }


    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }


    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );
    }


    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee employee = getEmployeeById(id);

        employee.setName(updatedEmployee.getName());
        employee.setAge(updatedEmployee.getAge());
        employee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(employee);
    }


    @Transactional
    public Employee uploadFiles(
            Long id,
            MultipartFile image,
            MultipartFile file
    ) throws IOException {

        Employee employee = getEmployeeById(id);

        String oldImageKey = employee.getImageKey();
        String oldFileKey = employee.getFileKey();

        String newImageKey = null;
        String newFileKey = null;

        try {

            if (image != null && !image.isEmpty()) {
                newImageKey = s3Service.uploadFile(
                        image, id, "images"
                );
            }

            if (file != null && !file.isEmpty()) {
                newFileKey = s3Service.uploadFile(
                        file, id, "documents"
                );
            }

            if (newImageKey != null) {
                employee.setImageKey(newImageKey);
            }

            if (newFileKey != null) {
                employee.setFileKey(newFileKey);
            }

            Employee savedEmployee = employeeRepository.save(employee);


            if (newImageKey != null && oldImageKey != null) {
                s3Service.deleteFile(oldImageKey);
            }

            if (newFileKey != null && oldFileKey != null) {
                s3Service.deleteFile(oldFileKey);
            }

            return savedEmployee;

        } catch (Exception exception) {

            // Clean up newly uploaded objects if the operation fails
            if (newImageKey != null) {
                try {
                    s3Service.deleteFile(newImageKey);
                } catch (Exception ignored) {
                }
            }

            if (newFileKey != null) {
                try {
                    s3Service.deleteFile(newFileKey);
                } catch (Exception ignored) {
                }
            }

            throw new RuntimeException(
                    "Unable to upload employee files",
                    exception
            );
        }
    }


    public byte[] downloadImage(Long id) {

        Employee employee = getEmployeeById(id);

        if (employee.getImageKey() == null) {
            throw new IllegalArgumentException(
                    "Employee image not found"
            );
        }

        return s3Service.downloadFile(employee.getImageKey());
    }


    public byte[] downloadDocument(Long id) {

        Employee employee = getEmployeeById(id);

        if (employee.getFileKey() == null) {
            throw new IllegalArgumentException(
                    "Employee document not found"
            );
        }

        return s3Service.downloadFile(employee.getFileKey());
    }


    public void deleteEmployee(Long id) {

        Employee employee = getEmployeeById(id);

        String imageKey = employee.getImageKey();
        String fileKey = employee.getFileKey();

        employeeRepository.delete(employee);

        if (imageKey != null) {
            s3Service.deleteFile(imageKey);
        }

        if (fileKey != null) {
            s3Service.deleteFile(fileKey);
        }
    }


    public void deleteImage(Long id) {

        Employee employee = getEmployeeById(id);

        if (employee.getImageKey() != null) {
            s3Service.deleteFile(employee.getImageKey());
            employee.setImageKey(null);
            employeeRepository.save(employee);
        }
    }


    public void deleteDocument(Long id) {

        Employee employee = getEmployeeById(id);

        if (employee.getFileKey() != null) {
            s3Service.deleteFile(employee.getFileKey());
            employee.setFileKey(null);
            employeeRepository.save(employee);
        }
    }
}