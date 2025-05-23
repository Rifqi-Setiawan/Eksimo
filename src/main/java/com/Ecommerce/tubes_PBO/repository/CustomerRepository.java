package com.Ecommerce.tubes_PBO.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.tubes_PBO.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
