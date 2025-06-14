package com.Ecommerce.tubes_PBO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerProfileDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String username;
}