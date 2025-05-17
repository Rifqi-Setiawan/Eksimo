package com.Ecommerce.tubes_PBO.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/product")
public class ProductController {
    ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{vendorId}")
    public Product getProduct(@RequestParam Long vendorId) {
        return productService.getProduct(vendorId);
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

    @DeleteMapping("/{vendorId}")
    public String deleteProduct(@PathVariable("vendorId") Long vendorId) {
        productService.deleteProduct(vendorId);
        return "Product berhasil dihapus";
    }
    
    
}
