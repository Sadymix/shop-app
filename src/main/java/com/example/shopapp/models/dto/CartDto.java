package com.example.shopapp.models.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class CartDto {
    BigDecimal totalPrice;
    List<ProductDto> productDtoList;
}
