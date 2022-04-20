package com.example.shopapp.mappers;

import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.dto.OrderDto;
import com.example.shopapp.models.entity.Cart;
import com.example.shopapp.models.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target="cartDto", source="order.cart")
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
    List<OrderDto> toDtoList(List<Order> orders);
    @Mapping(target = "productDtoList", source="cart.products")
    CartDto cartToCartDto(Cart cart);
}
