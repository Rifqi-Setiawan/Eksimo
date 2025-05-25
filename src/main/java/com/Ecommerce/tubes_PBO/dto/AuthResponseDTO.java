// src/main/java/com/Ecommerce/tubes_PBO/dto/AuthResponseDTO.java
package com.Ecommerce.tubes_PBO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String username; // email
    private Collection<? extends GrantedAuthority> authorities;
    private String tokenType = "Bearer";

    public AuthResponseDTO(String accessToken, String username, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.username = username;
        this.authorities = authorities;
    }
}