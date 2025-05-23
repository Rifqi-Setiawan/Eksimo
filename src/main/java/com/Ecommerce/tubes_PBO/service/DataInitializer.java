// com/Ecommerce/tubes_PBO/service/DataInitializer.java
package com.Ecommerce.tubes_PBO.service;

import com.Ecommerce.tubes_PBO.enums.UserRole;
import com.Ecommerce.tubes_PBO.model.Admin;
import com.Ecommerce.tubes_PBO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cek jika admin default belum ada
        if (!userRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created.");
        }
    }
}