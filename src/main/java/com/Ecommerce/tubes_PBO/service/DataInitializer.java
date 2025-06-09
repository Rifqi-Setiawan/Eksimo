// com/Ecommerce/tubes_PBO/service/DataInitializer.java
package com.Ecommerce.tubes_PBO.service;

import com.Ecommerce.tubes_PBO.model.Admin;
import com.Ecommerce.tubes_PBO.model.Category;
import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.repository.*;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(com.Ecommerce.tubes_PBO.enums.UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created. 🧑‍💼");
        }
        initializeCategories();
        initializeProducts();
    }

    private void initializeCategories() {
        List<String> categoryNames = Arrays.asList("Baju", "Celana", "Jaket");

        for (String name : categoryNames) {
            if (!categoryRepository.findByName(name).isPresent()) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
                System.out.println("Created category: " + name + " 🏷️");
            }
        }
    }

    private void initializeProducts() {
        // Ambil kategori dari database
        Optional<Category> bajuOpt = categoryRepository.findByName("Baju");
        Optional<Category> celanaOpt = categoryRepository.findByName("Celana");
        Optional<Category> jaketOpt = categoryRepository.findByName("Jaket");

        if (bajuOpt.isEmpty() || celanaOpt.isEmpty() || jaketOpt.isEmpty()) {
            System.out.println("Kategori belum lengkap, produk tidak diinisialisasi.");
            return;
        }

        Category baju = bajuOpt.get();
        Category celana = celanaOpt.get();
        Category jaket = jaketOpt.get();

        Product p1 = new Product();
        p1.setName("Kaos Polos");
        p1.setDescription("Kaos polos bahan katun, nyaman dipakai.");
        p1.setPrice(50000);
        p1.setStock(100);
        p1.setImages(List.of("https://example.com/kaos1.jpg"));
        p1.setCategory(baju);

        Product p2 = new Product();
        p2.setName("Celana Jeans");
        p2.setDescription("Celana jeans biru, cocok untuk sehari-hari.");
        p2.setPrice(120000);
        p2.setStock(50);
        p2.setImages(List.of("https://example.com/celana1.jpg"));
        p2.setCategory(celana);

        Product p3 = new Product();
        p3.setName("Jaket Hoodie");
        p3.setDescription("Jaket hoodie tebal, cocok untuk musim hujan.");
        p3.setPrice(150000);
        p3.setStock(30);
        p3.setImages(List.of("https://example.com/jaket1.jpg"));
        p3.setCategory(jaket);

        Product p4 = new Product();
        p4.setName("Kemeja Flanel");
        p4.setDescription("Kemeja flanel motif kotak.");
        p4.setPrice(90000);
        p4.setStock(40);
        p4.setImages(List.of("https://example.com/baju2.jpg"));
        p4.setCategory(baju);

        Product p5 = new Product();
        p5.setName("Celana Chino");
        p5.setDescription("Celana chino slim fit.");
        p5.setPrice(110000);
        p5.setStock(60);
        p5.setImages(List.of("https://example.com/celana2.jpg"));
        p5.setCategory(celana);

        productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        System.out.println("5 produk berhasil diinisialisasi. 🛒");
    }
}