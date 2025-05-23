package com.Ecommerce.tubes_PBO.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String name;   
    private String address;    
    private String phoneNumber;
}