package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.AddToCartRequestDTO;
import com.Ecommerce.tubes_PBO.dto.CartResponseDTO;
import com.Ecommerce.tubes_PBO.dto.CheckoutRequestDTO;
import com.Ecommerce.tubes_PBO.dto.CheckoutResponseDTO;
import com.Ecommerce.tubes_PBO.dto.CheckoutSingle;
import com.Ecommerce.tubes_PBO.dto.CustomerProfileDTO;
import com.Ecommerce.tubes_PBO.dto.OrderHistoryItemDTO;
import com.Ecommerce.tubes_PBO.dto.ProductListResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO;
import com.Ecommerce.tubes_PBO.dto.RegisterRequestDTO;
import com.Ecommerce.tubes_PBO.model.Customer;
import com.Ecommerce.tubes_PBO.model.Order;
import com.Ecommerce.tubes_PBO.model.OrderItem;
import com.Ecommerce.tubes_PBO.model.Product;
import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.repository.OrderRepository;
import com.Ecommerce.tubes_PBO.repository.ProductRepository;
import com.Ecommerce.tubes_PBO.repository.UserRepository;
import com.Ecommerce.tubes_PBO.service.AuthService;
import com.Ecommerce.tubes_PBO.service.CartService;
import com.Ecommerce.tubes_PBO.service.ProductService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final ProductService productService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public CustomerController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            authService.registerCustomer(registerRequestDTO);
            return ResponseEntity.ok("registrasi berhasil");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ProductListResponseDTO> getAllProducts() {
        ProductListResponseDTO productData = productService.getAllProducts();
        return ResponseEntity.ok(productData);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long productId) {
        ProductResponseDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/cart/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @Valid @RequestBody AddToCartRequestDTO requestDTO,
            Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.addItemToCart(username, requestDTO);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> getMyCart(Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.getCartByUsername(username);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.removeItemFromCart(username, cartItemId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cart/items/{cartItemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.updateItemQuantity(username, cartItemId, quantity);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CheckoutResponseDTO> checkout(
            @RequestBody CheckoutRequestDTO checkoutRequest,
            Authentication authentication) {
        String username = authentication.getName();

        // Ambil cart customer
        CartResponseDTO cart = cartService.getCartByUsername(username);

        // Validasi cart tidak kosong
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            CheckoutResponseDTO response = new CheckoutResponseDTO();
            response.setMessage("Cart is empty, cannot checkout.");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        // Buat Order baru
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(com.Ecommerce.tubes_PBO.enums.OrderStatus.PENDING);
        order.setTotalAmount(cart.getGrandTotal());
        order.setShippingAddress(checkoutRequest.getShippingAddress());
        order.setPaymentMethod(checkoutRequest.getPaymentMethod());
        order.setOrderNumber("ORD-" + System.currentTimeMillis());

        // Konversi CartItem ke OrderItem
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            // Ambil Product dari repository
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderItem.setProduct(product);

            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);

        // Simpan order ke database
        orderRepository.save(order);

        // Kosongkan cart customer (opsional)
        cartService.clearCart(username);

        // Response
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setOrderId(order.getId());
        response.setMessage("Checkout successful, order created.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CheckoutResponseDTO> checkoutSingleProduct(
            @PathVariable Long productId,
            @RequestBody CheckoutSingle checkoutRequest,
            Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int quantity = checkoutRequest.getQuantity() != null ? checkoutRequest.getQuantity() : 1;

        // Buat Order baru
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(com.Ecommerce.tubes_PBO.enums.OrderStatus.PENDING);
        order.setTotalAmount(quantity * product.getPrice());
        order.setShippingAddress(checkoutRequest.getShippingAddress());
        order.setPaymentMethod(checkoutRequest.getPaymentMethod());

        // Buat OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setTotalPrice(product.getPrice() * quantity);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());

        order.setOrderItems(List.of(orderItem));

        // Simpan order ke database
        orderRepository.save(order);

        // Response
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setOrderId(order.getId());
        response.setMessage("Checkout successful for single product.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<OrderHistoryItemDTO>> getOrderHistory(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        List<Order> orders = orderRepository.findByCustomer(customer);
        List<OrderHistoryItemDTO> history = orders.stream()
                .flatMap(order -> order.getOrderItems().stream().map(orderItem -> {
                    OrderHistoryItemDTO dto = new OrderHistoryItemDTO();
                    dto.setOrderId(order.getId());
                    dto.setProductName(orderItem.getProduct().getName());
                    dto.setProductImage(orderItem.getProduct().getImage());
                    dto.setStatus(order.getStatus().name());
                    dto.setTotal(orderItem.getTotalPrice());
                    return dto;
                }))
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!(user instanceof Customer customer)) {
            return ResponseEntity.badRequest().body("User is not a customer");
        }
        return ResponseEntity.ok(new CustomerProfileDTO(customer.getName(), customer.getAddress(),
                customer.getPhoneNumber(), customer.getUsername()));
    }
}