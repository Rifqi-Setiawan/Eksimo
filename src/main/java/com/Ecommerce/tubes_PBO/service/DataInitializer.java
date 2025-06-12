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
    List<String> categoryNames = Arrays.asList("Baju", "Celana", "Jaket"); // <-- perbaiki di sini

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
        p1.setName("Topi");
        p1.setDescription("Topi ganteng untuk anak kalcer.");
        p1.setPrice(50000);
        p1.setStock(100);
        p1.setImage("/images/products/topi.jpg"); 
        p1.setCategory(baju);

        Product p2 = new Product();
        p2.setName("Sweater");
        p2.setDescription("Sweater untuk pengangat tubuh.");
        p2.setPrice(120000);
        p2.setStock(50);
        p2.setImage("/images/products/sweater.jpg");
        p2.setCategory(celana);

        Product p3 = new Product();
        p3.setName("Kaos Kaki");
        p3.setDescription("Kaos kaki kalcer.");
        p3.setPrice(150000);
        p3.setStock(30);
        p3.setImage("/images/products/kaosKaki.jpg");
        p3.setCategory(jaket);

        Product p4 = new Product();
        p4.setName("Kaos");
        p4.setDescription("Kaos kalcer.");
        p4.setPrice(90000);
        p4.setStock(40);
        p4.setImage("/images/products/kaos.jpg");
        p4.setCategory(baju);

        productRepository.saveAll(List.of(p1, p2, p3, p4));
        System.out.println("5 produk berhasil diinisialisasi. 🛒");
    }
}