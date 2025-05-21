package com.Ecommerce.tubes_PBO.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.tubes_PBO.dto.UserResponse;
import com.Ecommerce.tubes_PBO.dto.LoginRequest;
import com.Ecommerce.tubes_PBO.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        UserResponse response = userService.login(request, httpRequest);
        System.out.println("Login berhasil untuk: " + response.getUsername());
        return ResponseEntity.ok(response);
    }

    // @PostMapping("/logout")
    // public ResponseEntity<String> logout() {
    // userService.logout();
    // return ResponseEntity.ok("Logout berhasil");
    // }

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as " + authentication.getName());
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

}
