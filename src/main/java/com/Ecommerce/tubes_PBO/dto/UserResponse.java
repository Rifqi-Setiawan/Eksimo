package com.Ecommerce.tubes_PBO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String username;
    private String role;
    private String message;
}
