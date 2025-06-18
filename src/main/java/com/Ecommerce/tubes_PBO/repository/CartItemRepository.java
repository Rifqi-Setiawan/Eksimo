package com.Ecommerce.tubes_PBO.repository;
import com.Ecommerce.tubes_PBO.model.Cart;
import com.Ecommerce.tubes_PBO.model.CartItem;
import com.Ecommerce.tubes_PBO.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}