package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.CartDto;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartControllerTest {

    private static final User USER = makePojo(User.class);
    private static final CartDto CART_DTO = makePojo(CartDto.class);

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CartService cartService;

    private MockMvc mockMvc;

    public CartControllerTest() {
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void getLastCartForCurrentUser() {
        when(cartService.getLastCart(eq(USER))).thenReturn(CART_DTO);
        var authenticationToken = mock(PreAuthenticatedAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(USER);
        mockMvc.perform(get("/api/cart")
                        .principal(authenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", equalTo(CART_DTO.getTotalPrice().longValueExact())))
                .andExpect(jsonPath("$.productDtoList.*", hasSize(CART_DTO.getProductDtoList().size())));
        verify(cartService).getLastCart(USER);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void addProductToLastCartForCurrentUser() {
        when(cartService.addProductToCart(1L, USER)).thenReturn(CART_DTO);
        var authenticationToken = mock(PreAuthenticatedAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(USER);
        mockMvc.perform(put("/api/cart/add/1")
                        .content(objectMapper.writeValueAsString(USER))
                        .principal(authenticationToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", equalTo(CART_DTO.getTotalPrice().longValueExact())))
                .andExpect(jsonPath("$.productDtoList.*", hasSize(CART_DTO.getProductDtoList().size())));
        verify(cartService).addProductToCart(1L, USER);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void removeProductFromCartForCurrentUser() {
        when(cartService.removeProductFromCart(1L, USER)).thenReturn(CART_DTO);
        var authenticationToken = mock(PreAuthenticatedAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(USER);
        mockMvc.perform(put("/api/cart/remove/1")
                        .content(objectMapper.writeValueAsString(USER))
                        .principal(authenticationToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", equalTo(CART_DTO.getTotalPrice().longValueExact())))
                .andExpect(jsonPath("$.productDtoList", hasSize(CART_DTO.getProductDtoList().size())));
        verify(cartService).removeProductFromCart(1L, USER);
    }
}
