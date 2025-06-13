package com.Ecommerce.tubes_PBO.dto;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class OrderAdminResponseDTO {
    private Long orderId;
    private String customerName;
    private String username;
    private String status;
    private Integer totalPrice;
    private Integer totalAmount;
    private LocalDateTime orderDate;
}