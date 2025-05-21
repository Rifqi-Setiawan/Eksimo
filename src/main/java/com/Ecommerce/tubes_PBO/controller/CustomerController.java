package com.Ecommerce.tubes_PBO.controller;


import com.Ecommerce.tubes_PBO.dto.RegisterRequest;

import com.Ecommerce.tubes_PBO.model.Customer;
import com.Ecommerce.tubes_PBO.service.CustomerService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(customerService.register(request));
    }
}