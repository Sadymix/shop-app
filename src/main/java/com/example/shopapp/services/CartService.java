package com.example.shopapp.services;

import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.mappers.CartMapper;
import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.entity.Cart;
import com.example.shopapp.models.entity.Product;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.CartRepo;
import com.example.shopapp.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final CartMapper cartMapper;
    private final ProductRepo productRepo;

    public CartDto getLastCart(User user) {
        var lastCart = getCart(user);
        return cartMapper.toDto(lastCart);
    }

    public CartDto addProductToCart(Long id, User user) {
        var lastCart = getCart(user);
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with this id not exist in database."));
        if (lastCart == null) {
            var cart = new Cart();
            var products = new ArrayList<Product>();
            products.add(product);
            setCart(cart, products, user);

            var saveCart = cartRepo.save(cart);
            return cartMapper.toDto(saveCart);
        }
        var products = addAllFromCartToList(lastCart);
        products.add(product);
        setCart(lastCart, products, user);
        var saveCart = cartRepo.save(lastCart);
        return cartMapper.toDto(saveCart);
    }


    public CartDto removeProductFromCart(Long id, User user) {
        var cartsForCurrentUser = cartRepo.findAllByUserId(user.getId());
        var cart = cartsForCurrentUser.get(cartsForCurrentUser.size() - 1);
        var product = productRepo.findById(id);
        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Product with this id not exist in database.");
        }
        var products = addAllFromCartToList(cart);
        products.remove(product.get());
        cart.setProducts(products);
        cart.setTotalPrice(getCartTotalPrice(cart));
        var cartSave = cartRepo.save(cart);
        return cartMapper.toDto(cartSave);
    }

    private Cart getCart(User user) {
        var cartsForCurrentUser = cartRepo
                .findAllByUserId(user.getId());
        return cartsForCurrentUser.get(cartsForCurrentUser.size() - 1);
    }

    private BigDecimal getCartTotalPrice(Cart cart) {
        return cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Product> addAllFromCartToList(Cart cart) {
        return new ArrayList<>(cart.getProducts());
    }

    private void setCart(Cart cart, List<Product> products, User user) {
        cart.setProducts(products);
        cart.setTotalPrice(getCartTotalPrice(cart));
        cart.setUser(user);
    }
}
