package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.OrderAdminResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductRequestDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;
import com.Ecommerce.tubes_PBO.model.Order;
import com.Ecommerce.tubes_PBO.repository.OrderRepository;
import com.Ecommerce.tubes_PBO.service.ProductService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderRepository orderRepository;

    public AdminController(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestPart("product") @Valid ProductRequestDTO productRequestDTO,
            @RequestPart("image") MultipartFile image) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO, image);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<ProductListResponseDTO> getAllProducts() {
        ProductListResponseDTO productData = productService.getAllProducts();
        return ResponseEntity.ok(productData);
    }

    @PutMapping("/products/{productId}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") ProductRequestDTO productRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return productService.updateProduct(productId, productRequestDTO, image);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderAdminResponseDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderAdminResponseDTO> dtos = orders.stream().map(order -> {
            OrderAdminResponseDTO dto = new OrderAdminResponseDTO();
            dto.setOrderId(order.getId());
            dto.setCustomerName(order.getCustomer().getName());
            dto.setUsername(order.getCustomer().getUsername());
            dto.setStatus(order.getStatus().name());
            dto.setTotalPrice(order.getTotalAmount());
            dto.setTotalAmount(order.getOrderItems().stream().mapToInt(item -> item.getQuantity()).sum());
            dto.setOrderDate(order.getOrderDate());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("status") String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(com.Ecommerce.tubes_PBO.enums.OrderStatus.valueOf(status));
        orderRepository.save(order);
        return ResponseEntity.ok("Order status updated to " + status);
    }

}
