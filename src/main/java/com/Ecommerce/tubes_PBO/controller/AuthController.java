package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.AuthResponseDTO;
import com.Ecommerce.tubes_PBO.dto.LoginRequestDTO;
import com.Ecommerce.tubes_PBO.security.JwtUtil; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; 

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.Ecommerce.tubes_PBO.security.CustomUserDetailsService userDetailsService; 

    @Autowired
    private com.Ecommerce.tubes_PBO.service.AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponseDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logoutUser();
        return ResponseEntity.ok("Logout successful");
    }
}