package com.Ecommerce.tubes_PBO.controller;

import com.Ecommerce.tubes_PBO.dto.AddToCartRequestDTO;
import com.Ecommerce.tubes_PBO.dto.CartResponseDTO;
import com.Ecommerce.tubes_PBO.dto.ProductResponseDTO; //
import com.Ecommerce.tubes_PBO.dto.RegisterRequestDTO; //
import com.Ecommerce.tubes_PBO.service.AuthService; //
import com.Ecommerce.tubes_PBO.service.CartService;
import com.Ecommerce.tubes_PBO.service.ProductService; //

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; //
import org.springframework.data.domain.Pageable; //
import org.springframework.data.web.PageableDefault; //
import org.springframework.http.ResponseEntity; //
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*; //

@RestController
@RequestMapping("/api/customer") //
public class CustomerController {
    private final ProductService productService; //
    
    @Autowired //
    private AuthService authService; //

    @Autowired
    private CartService cartService;

    @Autowired
    public CustomerController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/register") //
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequestDTO registerRequestDTO) { //
        try {
            authService.registerCustomer(registerRequestDTO); //
            return ResponseEntity.ok("registrasi berhasil"); //
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); //
        }
    }

    @GetMapping("/products") //
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts( //
            @PageableDefault(size = 10, sort = "id") Pageable pageable) { //
        Page<ProductResponseDTO> products = productService.getAllProducts(pageable); //
        return ResponseEntity.ok(products); //
    }
    @GetMapping("/products/{productId}") //
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long productId) { //
        ProductResponseDTO product = productService.getProductById(productId); //
        return ResponseEntity.ok(product); //
    }

    // --- Cart Endpoints ---
    @PostMapping("/cart/items")
    @PreAuthorize("hasRole('CUSTOMER')")
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
}