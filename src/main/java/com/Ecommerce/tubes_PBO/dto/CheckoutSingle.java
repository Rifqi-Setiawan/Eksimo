package com.Ecommerce.tubes_PBO.dto;

import lombok.Data;

@Data
public class CheckoutSingle {
    private String shippingAddress;
    private String paymentMethod;
    private Integer quantity;
}
