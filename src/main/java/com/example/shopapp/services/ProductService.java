package com.example.shopapp.services;

import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.mappers.ProductMapperImpl;
import com.example.shopapp.models.dto.ProductDto;
import com.example.shopapp.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapperImpl productMapper;

    public List<ProductDto> getProducts() {
        var allProducts = productRepo.findAll();
        return productMapper.toDtoList(allProducts);
    }

    public ProductDto getProduct(UUID id) {
        var product = productRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.toDto(product);
    }

    public ProductDto addProduct(ProductDto productDto) {
        var product = productRepo.save(productMapper.toEntity(productDto));
        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(UUID id, ProductDto productDto) {
        var product = productRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with " + id + " doesn't exist"));
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor());
        product.setType(productDto.getType());
        var productSave = productRepo.save(product);
        return productMapper.toDto(productSave);
    }

    public void deleteProduct(UUID id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product with " + id + " doesn't exist");
        }
        productRepo.deleteById(id);
    }

}
