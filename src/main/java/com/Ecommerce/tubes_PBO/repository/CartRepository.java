package com.Ecommerce.tubes_PBO.repository;
import com.Ecommerce.tubes_PBO.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId);
    Optional<Cart> findByCustomerUsername(String username);
}