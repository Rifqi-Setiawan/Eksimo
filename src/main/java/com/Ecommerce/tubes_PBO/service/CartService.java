package com.Ecommerce.tubes_PBO.service;
import com.Ecommerce.tubes_PBO.dto.AddToCartRequestDTO;
import com.Ecommerce.tubes_PBO.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO addItemToCart(String username, AddToCartRequestDTO requestDTO);
    CartResponseDTO getCartByUsername(String username);
    CartResponseDTO removeItemFromCart(String username, Long cartItemId);
    CartResponseDTO updateItemQuantity(String username, Long cartItemId, Integer newQuantity);
}