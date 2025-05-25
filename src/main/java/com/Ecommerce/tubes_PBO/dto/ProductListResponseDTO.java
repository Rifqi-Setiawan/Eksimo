package com.Ecommerce.tubes_PBO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDTO {
    private List<ProductResponseDTO> products;
    private long totalProducts;
}