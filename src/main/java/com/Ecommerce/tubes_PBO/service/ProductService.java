package com.Ecommerce.tubes_PBO.service;

import java.util.List;

import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Long productId);
    ProductListResponseDTO getAllProducts();
    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long productId);
    List<ProductResponseDTO> getProductsByCategory(Long categoryId);
}