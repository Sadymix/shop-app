package com.example.shopapp.services;

import com.example.shopapp.mappers.CartMapperImpl;
import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.entity.Cart;
import com.example.shopapp.models.entity.Product;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.CartRepo;
import com.example.shopapp.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final CartDto CART_DTO = makePojo(CartDto.class);
    private static final Cart CART = makePojo(Cart.class);
    private static final User USER = makePojo(User.class);
    private static final Product PRODUCT = makePojo(Product.class);

    @Mock
    private CartRepo cartRepo;
    @Mock
    private CartMapperImpl cartMapper;
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private CartService cartService;

    @Test
    void testGetLastCartForCurrentUser() {
        when(cartRepo.findAllByUserId(USER.getId())).thenReturn(List.of(CART));
        when(cartMapper.toDto(any(Cart.class))).thenReturn(CART_DTO);
        var cart = cartService.getLastCart(USER);
        verify(cartRepo).findAllByUserId(USER.getId());
        assertThat(cart).isEqualTo(CART_DTO);
    }


    @Test
    void testAddProductToCart() {
        when(cartRepo.findAllByUserId(any(Long.class))).thenReturn(List.of(CART));
        when(productRepo.findById(any(Long.class))).thenReturn(Optional.of(PRODUCT));
        when(cartRepo.save(any(Cart.class))).thenReturn(CART);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(CART_DTO);
        var cartDto = cartService.addProductToCart(PRODUCT.getId(), CART.getUser());
        verify(cartRepo).findAllByUserId(CART.getUser().getId());
        verify(productRepo).findById(PRODUCT.getId());
        verify(cartRepo).save(CART);
        assertThat(cartDto).isEqualTo(CART_DTO);
    }

    @Test
    void testRemoveProductFromCart() {
        when(cartRepo.findAllByUserId(any(Long.class))).thenReturn(List.of(CART));
        when(productRepo.findById(any(Long.class))).thenReturn(Optional.of(PRODUCT));
        when(cartRepo.save(any(Cart.class))).thenReturn(CART);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(CART_DTO);
        var cartDto = cartService.removeProductFromCart(PRODUCT.getId(), CART.getUser());
        verify(cartRepo).findAllByUserId(CART.getUser().getId());
        verify(productRepo).findById(PRODUCT.getId());
        verify(cartRepo).save(CART);
        assertThat(cartDto).isEqualTo(CART_DTO);
    }
}
