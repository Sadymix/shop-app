package com.example.shopapp.models.dto;

import com.example.shopapp.models.entity.Product;
import lombok.Builder;
import lombok.Value;


import java.math.BigDecimal;

@Value
@Builder
public class ProductDto {
    String name;
    BigDecimal price;
    String color;
    Product.TypeOfProduct type;
}
