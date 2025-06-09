package com.Ecommerce.tubes_PBO.service.impl;

import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;
import com.Ecommerce.tubes_PBO.exception.ResourceNotFoundException;
import com.Ecommerce.tubes_PBO.model.Category;
import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.repository.CategoryRepository;
import com.Ecommerce.tubes_PBO.repository.ProductRepository;
import com.Ecommerce.tubes_PBO.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + productRequestDTO.getCategoryId()));

        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setCategory(category);
        product.setImages(productRequestDTO.getImages());
        product.setAverageRating(0.0);

        Product savedProduct = productRepository.save(product);
        return mapToProductResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return mapToProductResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponseDTO getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDTO> productDTOs = productList.stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
        long totalProducts = productRepository.count();
        return new ProductListResponseDTO(productDTOs, totalProducts);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + productRequestDTO.getCategoryId()));

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setCategory(category);
        product.setImages(productRequestDTO.getImages());

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImages(product.getImages());
        dto.setAverageRating(product.getAverageRating());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            ProductResponseDTO.CategoryInfoDTO categoryInfo = new ProductResponseDTO.CategoryInfoDTO();
            categoryInfo.setId(product.getCategory().getId());
            categoryInfo.setName(product.getCategory().getName());
            dto.setCategory(categoryInfo);
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }
}
