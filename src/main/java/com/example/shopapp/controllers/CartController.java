package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'STAFF')")
public class CartController {

    private final CartService cartService;


    @GetMapping
    public CartDto getLastCartForCurrentUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            return cartService.getLastCart(user);
        }
        throw new IllegalStateException("The token does not contain authorized user data.");
    }

    @PostMapping("add/{id}")
    public CartDto addProductToLastCartForCurrentUser(@Valid @PathVariable Long id, Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            return cartService.addProductToCart(id, user);
        }
        throw new IllegalStateException("The token does not contain authorized user data.");
    }

    @PostMapping("remove/{id}")
    public CartDto removeProductFromCartForCurrentUser(@Valid @PathVariable Long id,
                                                       Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            return cartService.removeProductFromCart(id, user);
        }
        throw new IllegalStateException("The token does not contain authorized user data.");
    }
}
