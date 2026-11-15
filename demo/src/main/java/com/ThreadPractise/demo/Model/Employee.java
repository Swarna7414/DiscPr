package com.ThreadPractise.demo.Model;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Employee")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    private Double salary;

}