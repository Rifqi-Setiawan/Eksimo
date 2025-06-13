package com.Ecommerce.tubes_PBO.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, MultipartFile image);
    ProductResponseDTO getProductById(Long productId);
    ProductListResponseDTO getAllProducts();
    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, MultipartFile image);
    void deleteProduct(Long productId);
    List<ProductResponseDTO> getProductsByCategory(Long categoryId);
}