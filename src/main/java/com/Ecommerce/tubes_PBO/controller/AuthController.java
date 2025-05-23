// com/Ecommerce/tubes_PBO/controller/AuthController.java
package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.AuthResponseDTO;
import com.Ecommerce.tubes_PBO.dto.LoginRequestDTO;
import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.security.CustomUserDetailsService;
import com.Ecommerce.tubes_PBO.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authService.loginUser(loginRequestDTO);

            CustomUserDetailsService.UserPrincipal userPrincipal =
                (CustomUserDetailsService.UserPrincipal) authentication.getPrincipal();
            User loggedInUser = userPrincipal.getUser();

            return ResponseEntity.ok(new AuthResponseDTO("User logged in successfully!", loggedInUser.getUsername(), loggedInUser.getRole().name()));
        } catch (Exception e) {
            // Sebaiknya log error di sini
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid credentials or user not found.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Dapatkan informasi autentikasi pengguna saat ini
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new AuthResponseDTO("User logged out successfully!", null, null));
    }
}