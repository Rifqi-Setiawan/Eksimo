package com.Ecommerce.tubes_PBO.service.impl;

import com.Ecommerce.tubes_PBO.dto.AddToCartRequestDTO;
import com.Ecommerce.tubes_PBO.dto.CartItemResponseDTO;
import com.Ecommerce.tubes_PBO.dto.CartResponseDTO;
import com.Ecommerce.tubes_PBO.exception.BadRequestException;
import com.Ecommerce.tubes_PBO.exception.ResourceNotFoundException;
import com.Ecommerce.tubes_PBO.model.*;
import com.Ecommerce.tubes_PBO.repository.*;
import com.Ecommerce.tubes_PBO.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(String username, AddToCartRequestDTO requestDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!(user instanceof Customer)) {
            throw new BadRequestException("User is not a customer and cannot have a cart.");
        }
        Customer customer = (Customer) user;

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDTO.getProductId()));

        if (product.getStock() == null || product.getStock() < requestDTO.getQuantity()) {
            throw new BadRequestException("Product stock (" + product.getStock()
                    + ") is not sufficient for requested quantity (" + requestDTO.getQuantity() + ").");
        }

        Cart cart = customer.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            customer.setCart(cart);
        }
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + requestDTO.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new BadRequestException("Product stock (" + product.getStock()
                        + ") is not sufficient for updated total quantity (" + newQuantity + ").");
            }
            existingItem.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(requestDTO.getQuantity());
            newItem.setPrice(product.getPrice());
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);

        return mapCartToResponseDTO(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCartByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!(user instanceof Customer)) {
            throw new BadRequestException("User is not a customer.");
        }
        Customer customer = (Customer) user;
        Cart cart = customer.getCart();
        if (cart == null) {
            CartResponseDTO emptyCartDto = new CartResponseDTO();
            emptyCartDto.setItems(List.of());
            emptyCartDto.setGrandTotal(0);
            emptyCartDto.setTotalUniqueItems(0);
            emptyCartDto.setTotalItemUnits(0);
            return emptyCartDto;
        }
        return mapCartToResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Customer customer = (Customer) user;
        Cart cart = customer.getCart();

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId + " in user's cart"));

        cart.getItems().remove(itemToRemove);
        cartRepository.save(cart);

        return mapCartToResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(String username, Long cartItemId, Integer newQuantity) {
        if (newQuantity <= 0) {
            return removeItemFromCart(username, cartItemId);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Customer customer = (Customer) user;
        Cart cart = customer.getCart();

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId + " in user's cart"));

        Product product = cartItem.getProduct();
        if (product.getStock() == null || product.getStock() < newQuantity) {
            throw new BadRequestException("Product stock (" + product.getStock()
                    + ") is not sufficient for requested quantity (" + newQuantity + ").");
        }

        cartItem.setQuantity(newQuantity);
        cartRepository.save(cart);

        return mapCartToResponseDTO(cart);
    }

    private CartResponseDTO mapCartToResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getId());

        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream().map(item -> { 
            CartItemResponseDTO itemDto = new CartItemResponseDTO();
            itemDto.setCartItemId(item.getId());

            Product product = item.getProduct();
            itemDto.setProductId(product.getId());
            itemDto.setProductName(product.getName());
            itemDto.setProductImageUrl(product.getImage());

            itemDto.setQuantity(item.getQuantity());
            itemDto.setPricePerUnit(item.getPrice());
            if (item.getPrice() != null && item.getQuantity() != null) {
                itemDto.setSubtotal(item.getPrice() * item.getQuantity());
            } else {
                itemDto.setSubtotal(0);
            }

            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs); //
        dto.setGrandTotal(cart.getTotalPrice()); //
        dto.setTotalUniqueItems(cart.getItems().size()); //
        dto.setTotalItemUnits(cart.getItems().stream().mapToInt(CartItem::getQuantity).sum()); //

        return dto;
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!(user instanceof Customer)) {
            throw new BadRequestException("User is not a customer.");
        }
        Customer customer = (Customer) user;
        Cart cart = customer.getCart();
        if (cart != null && cart.getItems() != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }
}
