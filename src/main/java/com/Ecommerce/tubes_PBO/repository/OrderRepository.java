package com.Ecommerce.tubes_PBO.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.tubes_PBO.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
