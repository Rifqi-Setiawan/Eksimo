package com.Ecommerce.tubes_PBO.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.tubes_PBO.model.Customer;
import com.Ecommerce.tubes_PBO.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomer(Customer customer);
}
