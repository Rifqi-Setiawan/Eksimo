package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;
import com.Ecommerce.tubes_PBO.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products") 
    public ResponseEntity<ProductListResponseDTO> getAllProducts() { 
        ProductListResponseDTO productData = productService.getAllProducts();
        return ResponseEntity.ok(productData);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long productId) {
        ProductResponseDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(productId, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok("Daftar semua user (untuk admin)");
    }
}
