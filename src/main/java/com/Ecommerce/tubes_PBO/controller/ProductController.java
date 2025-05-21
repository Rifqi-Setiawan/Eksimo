package com.Ecommerce.tubes_PBO.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.tubes_PBO.dto.ResponseHandler;
import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProduct(id);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, product);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping("")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @PostMapping("")
    public String createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return "Product berhasil ditambahkan";
    }

    @PutMapping("")
    public String updateProduct(@RequestBody Product produt) {
        productService.updateProduct(produt);
        return "Product berhasil diupdate";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product berhasil dihapus";
    }

}
