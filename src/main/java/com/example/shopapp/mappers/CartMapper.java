package com.example.shopapp.mappers;

import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "productDtoList", source="cart.products")
    CartDto toDto(Cart cart);
}
