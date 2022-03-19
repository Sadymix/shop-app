package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.ProductDto;
import com.example.shopapp.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    private static final ProductDto PRODUCT_DTO = makePojo(ProductDto.class);
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private MockMvc mockMvc;

    ProductControllerTest() {
    }

    @BeforeEach
    public void setUP() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @WithMockUser(authorities = "USER")
    @SneakyThrows
    @Test
    void testGetProducts() {
        when(productService.getProducts()).thenReturn(List.of(PRODUCT_DTO));
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        verify(productService).getProducts();
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testGetProduct() {
        when(productService.getProduct(1L)).thenReturn(PRODUCT_DTO);
        mockMvc.perform(get("/api/products/1")
                        .content(objectMapper.writeValueAsString(PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(PRODUCT_DTO.getName())))
                .andExpect(jsonPath("$.price").value(PRODUCT_DTO.getPrice()))
                .andExpect(jsonPath("$.color", equalTo(PRODUCT_DTO.getColor())))
                .andExpect(jsonPath("$.type").value(PRODUCT_DTO.getType().name()));
        verify(productService).getProduct(1L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testAddProduct() {
        when(productService.addProduct(any(ProductDto.class))).thenReturn(PRODUCT_DTO);
        mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(PRODUCT_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(PRODUCT_DTO.getName())))
                .andExpect(jsonPath("$.price").value(PRODUCT_DTO.getPrice()))
                .andExpect(jsonPath("$.color", equalTo(PRODUCT_DTO.getColor())))
                .andExpect(jsonPath("$.type").value(PRODUCT_DTO.getType().name()));
        verify(productService).addProduct(PRODUCT_DTO);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testUpdateProduct() {
        when(productService.updateProduct(1L, PRODUCT_DTO)).thenReturn(PRODUCT_DTO);
        mockMvc.perform(put("/api/products/1")
                .content(objectMapper.writeValueAsString(PRODUCT_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(PRODUCT_DTO.getName())))
                .andExpect(jsonPath("$.price").value(PRODUCT_DTO.getPrice()))
                .andExpect(jsonPath("$.color", equalTo(PRODUCT_DTO.getColor())))
                .andExpect(jsonPath("$.type").value(PRODUCT_DTO.getType().name()));
        verify(productService).updateProduct(1L, PRODUCT_DTO);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
        verify(productService).deleteProduct(1L);
    }
}
