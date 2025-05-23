package com.Ecommerce.tubes_PBO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.Long;
import com.Ecommerce.tubes_PBO.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
