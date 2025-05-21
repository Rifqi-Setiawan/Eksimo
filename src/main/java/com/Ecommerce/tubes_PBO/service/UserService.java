package com.Ecommerce.tubes_PBO.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecommerce.tubes_PBO.dto.LoginRequest;
import com.Ecommerce.tubes_PBO.dto.UserResponse;
import com.Ecommerce.tubes_PBO.exception.AuthException;
import com.Ecommerce.tubes_PBO.model.Admin;
import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.repo.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public UserService(UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        try {
            System.out.println("Attempting to login user: " + request.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AuthException("User not found"));

            System.out.println("User found: " + user.getUsername() + " with role: " + user.getRole());

            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    "Login sukses");
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            throw new AuthException("Invalid username or password");
        }
    }

    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @PostConstruct
    public void initAdminUser() {
        if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
            Admin admin = new Admin();
            admin.setUsername(ADMIN_USERNAME);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}
