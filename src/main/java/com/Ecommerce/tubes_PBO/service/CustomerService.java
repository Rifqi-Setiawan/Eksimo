package com.Ecommerce.tubes_PBO.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecommerce.tubes_PBO.dto.RegisterRequest;
import com.Ecommerce.tubes_PBO.repo.CustomerRepository;
import com.Ecommerce.tubes_PBO.model.Customer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer register(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole("CUSTOMER");
        customer.setCustId("CUST-" + System.currentTimeMillis());
        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setPhoneNumber(request.getPhoneNumber());
        return customerRepository.save(customer);
    }
}
