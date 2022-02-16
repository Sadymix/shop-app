package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.ProductDto;
import com.example.shopapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable UUID id) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "Deleted product of id: " + id;
    }

}
