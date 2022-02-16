package com.example.shopapp.services;

import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.mappers.ProductMapperImpl;
import com.example.shopapp.models.dto.ProductDto;
import com.example.shopapp.models.entity.Product;
import com.example.shopapp.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final ProductDto PRODUCT_DTO = makePojo(ProductDto.class);
    private static final Product PRODUCT = makePojo(Product.class);

    @Mock
    private ProductRepo productRepo;
    @Mock
    private ProductMapperImpl productMapper;
    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProducts() {
        var productList = List.of(PRODUCT);
        when(productRepo.findAll()).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(List.of(PRODUCT_DTO));
        var testProductList = productService.getProducts();
        verify(productRepo).findAll();
        verify(productMapper).toDtoList(any());
        assertThat(testProductList).isEqualTo(List.of(PRODUCT_DTO));
    }

    @Test
    void testGetProduct() {
        when(productRepo.findById(PRODUCT.getId())).thenReturn(Optional.ofNullable(PRODUCT));
        when(productMapper.toDto(PRODUCT)).thenReturn(PRODUCT_DTO);
        var testProduct = productService.getProduct(PRODUCT.getId());
        verify(productRepo).findById(PRODUCT.getId());
        verify(productMapper).toDto(PRODUCT);
        assertThat(testProduct).isEqualTo(PRODUCT_DTO);
    }

    @Test
    void testAddProduct() {
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(PRODUCT);
        when(productRepo.save(any(Product.class))).thenReturn(PRODUCT);
        when(productMapper.toDto(any(Product.class))).thenReturn(PRODUCT_DTO);
        var testProduct = productService.addProduct(PRODUCT_DTO);
        verify(productMapper).toEntity(PRODUCT_DTO);
        verify(productRepo).save(PRODUCT);
        verify(productMapper).toDto(PRODUCT);
        assertThat(testProduct).isEqualTo(PRODUCT_DTO);
    }

    @Test
    void testUpdateProduct() {
        when(productRepo.findById(any(UUID.class))).thenReturn(Optional.ofNullable(PRODUCT));
        when(productRepo.save(any(Product.class))).thenReturn(PRODUCT);
        when(productMapper.toDto(any(Product.class))).thenReturn(PRODUCT_DTO);
        var testProduct = productService.updateProduct(PRODUCT.getId(), PRODUCT_DTO);
        verify(productRepo).findById(PRODUCT.getId());
        verify(productRepo).save(PRODUCT);
        verify(productMapper).toDto(PRODUCT);
        assertThat(testProduct).isEqualTo(PRODUCT_DTO);
    }

    @Test
    void testDeleteProductIfIdExist() {
        when(productRepo.existsById(any(UUID.class))).thenReturn(Boolean.TRUE);
        doNothing().when(productRepo).deleteById(PRODUCT.getId());
        productService.deleteProduct(PRODUCT.getId());
        verify(productRepo).deleteById(PRODUCT.getId());
    }

    @Test
    void testDeleteProductIfIdDoesntExist() {
        when(productRepo.existsById(any(UUID.class))).thenReturn(Boolean.FALSE);
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(PRODUCT.getId()));
        verify(productRepo, times(0)).deleteById(PRODUCT.getId());
    }
}