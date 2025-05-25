package com.Ecommerce.tubes_PBO.dto;
import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Integer quantity;
    private Integer pricePerUnit; 
    private Integer subtotal;     
}