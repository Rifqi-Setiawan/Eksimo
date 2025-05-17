package com.Ecommerce.tubes_PBO.service;

import java.util.List;

import com.Ecommerce.tubes_PBO.model.Product;

public interface ProductService {
    public String createProduct(Product product);
    public String updateProduct(Product product);
    public String deleteProduct(Long productId);
    public Product getProduct(Long productId);
    public List<Product> getAllProduct();
}
