package com.Ecommerce.tubes_PBO.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;
    private CategoryInfoDTO category; 
    private String image; // ubah dari images ke image
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data 
    public static class CategoryInfoDTO {
        private Long id;
        private String name;
    }
}