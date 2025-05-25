// com/Ecommerce/tubes_PBO/controller/CustomerController.java
package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.AuthResponseDTO;
import com.Ecommerce.tubes_PBO.dto.RegisterRequestDTO;
import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.service.AuthService; // AuthService digunakan untuk registrasi
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private AuthService authService; 

    @PostMapping("/register") 
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            authService.registerCustomer(registerRequestDTO);
            return ResponseEntity.ok("registrasi berhasil");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}