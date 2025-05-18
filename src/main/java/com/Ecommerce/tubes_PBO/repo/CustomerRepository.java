package com.Ecommerce.tubes_PBO.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.tubes_PBO.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustId(String custId);
    boolean existsByPhoneNumber(String phoneNumber);
}
