package com.Ecommerce.tubes_PBO.dto;

import lombok.Data;

@Data
public class OrderHistoryItemDTO {
    private Long orderId;
    private String productName;
    private String productImage;
    private String status;
    private Integer total;
}