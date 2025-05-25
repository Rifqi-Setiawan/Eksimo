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

import java.math.BigDecimal;
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

    @Autowired
    private CartItemRepository cartItemRepository;

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
        // Seharusnya cart sudah ada karena diinisialisasi di konstruktor Customer
        if (cart == null) {
            // Fallback jika konstruktor belum diupdate atau ada kasus lain
            cart = new Cart();
            cart.setCustomer(customer);
            customer.setCart(cart); // Pastikan relasi dua arah
            // cartRepository.save(cart); // Akan tersimpan saat customer disimpan jika
            // cascade benar
        }

        // Cek apakah item produk ini sudah ada di list items cart
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
            // existingItem.setPrice(product.getPrice()); // Update harga jika harga produk
            // berubah? Atau biarkan harga saat pertama kali masuk?
            // Umumnya harga di cart item tidak diupdate otomatis.
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(requestDTO.getQuantity());
            newItem.setPrice(product.getPrice()); // Simpan harga produk saat ini
            cart.getItems().add(newItem);
        }

        // Jika totalPrice di Cart adalah kolom database, panggil recalculate dan save
        // cart.recalculateTotalPrice();
        // cartRepository.save(cart); // Cascade akan menyimpan CartItems juga

        // Karena Cart di-cascade dari Customer, menyimpan Customer akan menyimpan Cart
        // & CartItems.
        // Atau, jika Cart diambil langsung, simpan Cart.
        // Untuk memastikan, kita bisa save Cart secara eksplisit jika diambil terpisah
        // atau dimodifikasi.
        // Jika Cart selalu diakses melalui customer.getCart(), maka perubahan pada Cart
        // (seperti add item) akan tersimpan
        // saat transaksi selesai jika customer adalah managed entity.
        // Untuk lebih eksplisit, jika Cart adalah entitas yang di-manage:
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

        cart.getItems().remove(itemToRemove); // orphanRemoval=true akan menghapus CartItem dari DB

        // Jika totalPrice di Cart adalah kolom database, panggil recalculate dan save
        // cart.recalculateTotalPrice();
        cartRepository.save(cart);

        return mapCartToResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(String username, Long cartItemId, Integer newQuantity) {
        if (newQuantity <= 0) {
            // Jika kuantitas baru 0 atau kurang, anggap sebagai penghapusan item
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

        // Jika totalPrice di Cart adalah kolom database, panggil recalculate dan save
        // cart.recalculateTotalPrice();
        cartRepository.save(cart); // Menyimpan perubahan pada Cart (dan CartItem via cascade atau karena CartItem
                                   // adalah managed entity)

        return mapCartToResponseDTO(cart);
    }

    // Helper method untuk mapping
    private CartResponseDTO mapCartToResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO(); //
        dto.setCartId(cart.getId()); //

        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream().map(item -> { //
            CartItemResponseDTO itemDto = new CartItemResponseDTO();
            itemDto.setCartItemId(item.getId()); //

            Product product = item.getProduct(); // Product di CartItem EAGER fetched atau di-load jika LAZY
            itemDto.setProductId(product.getId()); //
            itemDto.setProductName(product.getName()); //

            // Mengambil gambar pertama dari list 'images' jika ada
            if (product.getImages() != null && !product.getImages().isEmpty()) { //
                itemDto.setProductImageUrl(product.getImages().get(0)); //
            } else {
                itemDto.setProductImageUrl(null); // Atau string kosong jika prefer
            }

            itemDto.setQuantity(item.getQuantity()); //
            itemDto.setPricePerUnit(item.getPrice()); // item.getPrice() adalah Integer dari CartItem.price

            // Kalkulasi subtotal sebagai Integer
            if (item.getPrice() != null && item.getQuantity() != null) {
                itemDto.setSubtotal(item.getPrice() * item.getQuantity()); //
            } else {
                itemDto.setSubtotal(0); // Default jika price atau quantity null
            }

            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs); //
        dto.setGrandTotal(cart.getTotalPrice()); // Menggunakan method transient dari Cart, yang mengembalikan Integer
        dto.setTotalUniqueItems(cart.getItems().size()); //
        dto.setTotalItemUnits(cart.getItems().stream().mapToInt(CartItem::getQuantity).sum()); //

        return dto;
    }
}
