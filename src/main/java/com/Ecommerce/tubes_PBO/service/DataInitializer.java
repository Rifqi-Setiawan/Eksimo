// com/Ecommerce/tubes_PBO/service/DataInitializer.java
package com.Ecommerce.tubes_PBO.service;

import com.Ecommerce.tubes_PBO.model.Admin;
import com.Ecommerce.tubes_PBO.model.Category; 
import com.Ecommerce.tubes_PBO.repository.CategoryRepository; 
import com.Ecommerce.tubes_PBO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository; // Inject CategoryRepository

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

        // Inisialisasi Kategori Produk Default
        initializeCategories();
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
}