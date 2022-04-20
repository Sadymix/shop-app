package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.OrderDto;
import com.example.shopapp.models.entity.Order;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.services.OrderService;
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

import java.util.List;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OderControllerTest {

    private static final OrderDto ORDER_DTO = makePojo(OrderDto.class);
    private static final OrderDto ORDER_DTO_1 = makePojo(OrderDto.class);
    private static final User USER = makePojo(User.class);
    private static final OrderDto ORDER_DTO_IN_PROGRESS = OrderDto.builder()
            .orderStatus(Order.OrderStatus.IN_PROGRESS)
            .build();
    private static final OrderDto ORDER_DTO_SHIPPED = OrderDto.builder()
            .orderStatus(Order.OrderStatus.SHIPPED)
            .build();
    private static final OrderDto ORDER_DTO_DELIVERED = OrderDto.builder()
            .orderStatus(Order.OrderStatus.DELIVERED)
            .build();
    private static final OrderDto ORDER_DTO_CANCELED = OrderDto.builder()
            .orderStatus(Order.OrderStatus.CANCELED)
            .build();

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @SneakyThrows
    @WithMockUser(authorities = "STAFF")
    @Test
    void getOrder() {
        when(orderService.getOrder(1L)).thenReturn(ORDER_DTO);
        mockMvc.perform(get("/api/order/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", equalTo(ORDER_DTO.getFirstName())))
                .andExpect(jsonPath("lastName", equalTo(ORDER_DTO.getLastName())))
                .andExpect(jsonPath("address", equalTo(ORDER_DTO.getAddress())))
                .andExpect(jsonPath("city", equalTo(ORDER_DTO.getCity())))
                .andExpect(jsonPath("postalCode", equalTo(ORDER_DTO.getPostalCode())))
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO.getOrderStatus()))))
                .andExpect(jsonPath("cartDto.totalPrice", equalTo(ORDER_DTO.getCartDto().getTotalPrice().longValueExact())))
                .andExpect(jsonPath("cartDto.productDtoList", hasSize(ORDER_DTO.getCartDto().getProductDtoList().size())));
        verify(orderService).getOrder(1L);
    }

    @WithMockUser(authorities = "USER")
    @SneakyThrows
    @Test
    void getOrdersForCurrentUser() {
        when(orderService.getOrdersForCurrentUser(USER.getId())).thenReturn(List.of(ORDER_DTO));
        var authenticationToken = mock(PreAuthenticatedAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(USER);
        mockMvc.perform(get("/api/order/user")
                        .principal(authenticationToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", equalTo(ORDER_DTO.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", equalTo(ORDER_DTO.getLastName())))
                .andExpect(jsonPath("$[0].address", equalTo(ORDER_DTO.getAddress())))
                .andExpect(jsonPath("$[0].city", equalTo(ORDER_DTO.getCity())))
                .andExpect(jsonPath("$[0].postalCode", equalTo(ORDER_DTO.getPostalCode())))
                .andExpect(jsonPath("$[0].orderStatus", equalTo(valueOf(ORDER_DTO.getOrderStatus()))))
                .andExpect(jsonPath("$[0].cartDto.totalPrice", equalTo(ORDER_DTO.getCartDto().getTotalPrice().longValueExact())))
                .andExpect(jsonPath("$[0].cartDto.productDtoList", hasSize(ORDER_DTO.getCartDto().getProductDtoList().size())));
        verify(orderService).getOrdersForCurrentUser(USER.getId());
    }

    @WithMockUser(authorities = "USER")
    @SneakyThrows
    @Test
    void addOrder() {
        when(orderService.addOrder(ORDER_DTO, USER)).thenReturn(ORDER_DTO_1);
        var authenticationToken = mock(PreAuthenticatedAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(USER);
        mockMvc.perform(post("/api/order")
                        .content(objectMapper.writeValueAsString(ORDER_DTO))
                        .content(objectMapper.writeValueAsString(ORDER_DTO))
                        .principal(authenticationToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", equalTo(ORDER_DTO_1.getFirstName())))
                .andExpect(jsonPath("lastName", equalTo(ORDER_DTO_1.getLastName())))
                .andExpect(jsonPath("address", equalTo(ORDER_DTO_1.getAddress())))
                .andExpect(jsonPath("city", equalTo(ORDER_DTO_1.getCity())))
                .andExpect(jsonPath("postalCode", equalTo(ORDER_DTO_1.getPostalCode())))
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO_1.getOrderStatus()))))
                .andExpect(jsonPath("cartDto.totalPrice", equalTo(ORDER_DTO_1.getCartDto().getTotalPrice().longValueExact())))
                .andExpect(jsonPath("cartDto.productDtoList", hasSize(ORDER_DTO_1.getCartDto().getProductDtoList().size())));
        verify(orderService).addOrder(ORDER_DTO, USER);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void deleteOrder() {
        doNothing().when(orderService).removeOrder(1L);
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());
        verify(orderService).removeOrder(1L);
    }

    @WithMockUser(authorities = "STAFF")
    @SneakyThrows
    @Test
    void orderInProgress() {
        when(orderService.orderInProgress(1L)).thenReturn(ORDER_DTO_IN_PROGRESS);
        mockMvc.perform(put("/api/order/1/in_progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO_IN_PROGRESS.getOrderStatus()))));
        verify(orderService).orderInProgress(1L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void orderShipped() {
        when(orderService.orderShipped(1L)).thenReturn(ORDER_DTO_SHIPPED);
        mockMvc.perform(put("/api/order/1/shipped"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO_SHIPPED.getOrderStatus()))));
        verify(orderService).orderShipped(1L);
    }

    @WithMockUser(authorities = "STAFF")
    @SneakyThrows
    @Test
    void orderDelivered() {
        when(orderService.orderDelivered(1L)).thenReturn(ORDER_DTO_DELIVERED);
        mockMvc.perform(put("/api/order/1/delivered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO_DELIVERED.getOrderStatus()))));
        verify(orderService).orderDelivered(1L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void orderCanceled() {
        when(orderService.orderCanceled(1L)).thenReturn(ORDER_DTO_CANCELED);
        mockMvc.perform(put("/api/order/1/canceled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus", equalTo(valueOf(ORDER_DTO_CANCELED.getOrderStatus()))));
        verify(orderService).orderCanceled(1L);
    }
}