// src/main/java/com/Ecommerce/tubes_PBO/service/AuthService.java
package com.Ecommerce.tubes_PBO.service;

import com.Ecommerce.tubes_PBO.dto.LoginRequestDTO;
import com.Ecommerce.tubes_PBO.dto.RegisterRequestDTO;
import com.Ecommerce.tubes_PBO.enums.UserRole; 
import com.Ecommerce.tubes_PBO.model.Customer;
import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registers a new customer.
     *
     * @param registerRequestDTO DTO containing registration data.
     * @return The created User (Customer).
     * @throws RuntimeException if the username is already taken.
     */
    @Transactional
    public User registerCustomer(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        Customer customer = new Customer();
        customer.setUsername(registerRequestDTO.getUsername());
        customer.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        customer.setName(registerRequestDTO.getName());
        customer.setAddress(registerRequestDTO.getAddress());
        customer.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        customer.setRole(com.Ecommerce.tubes_PBO.enums.UserRole.CUSTOMER); 

        return userRepository.save(customer);
    }

    /**
     * Authenticates a user based on login credentials.
     *
     * @param loginRequestDTO DTO containing login data (username and password).
     * @return Authentication object if successful.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    public Authentication loginUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(), 
                        loginRequestDTO.getPassword() 
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication); //
        return authentication; //
    }
}