package com.Ecommerce.tubes_PBO.dto;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    private String shippingAddress;
    private String paymentMethod;
}