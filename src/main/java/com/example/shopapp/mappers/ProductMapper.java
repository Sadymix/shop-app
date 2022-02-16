package com.example.shopapp.mappers;

import com.example.shopapp.models.dto.ProductDto;
import com.example.shopapp.models.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);
}
