package com.Ecommerce.tubes_PBO.dto;
import lombok.Data;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;
    private List<CartItemResponseDTO> items;
    private Integer totalUniqueItems;
    private Integer totalItemUnits;
    private Integer grandTotal; 
}