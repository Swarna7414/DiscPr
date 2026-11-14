package com.ThreadPractise.demo.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hit")
public class PRestController {

    @GetMapping("/check")
    public ResponseEntity<String> hitThisThing(){
        return ResponseEntity.ok("This is Working");
    }

}