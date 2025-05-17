package com.Ecommerce.tubes_PBO.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Ecommerce.tubes_PBO.exception.ProductNotFoundException;
import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.repo.ProductRepository;
import com.Ecommerce.tubes_PBO.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String createProduct(Product product) {
        productRepository.save(product);
        return "Berhasil menambahkan Product";
        
    }

    @Override
    public String updateProduct(Product product) {
        productRepository.save(product);
        return "Berhasil update Product";
    }

    @Override
    public String deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        return "Berhasil menghapus product";
    }

    @Override
    public Product getProduct(Long productId) {
        if(productRepository.findById(productId).isEmpty()){
            throw new ProductNotFoundException("Product tidak ditemukan");
        }
        return productRepository.findById(productId).get();
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

}
