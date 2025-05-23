package com.Ecommerce.tubes_PBO.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Long productId);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long productId);
}