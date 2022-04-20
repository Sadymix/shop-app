package com.example.shopapp.services;

import com.example.shopapp.mappers.OrderMapperImpl;
import com.example.shopapp.models.dto.OrderDto;
import com.example.shopapp.models.entity.Cart;
import com.example.shopapp.models.entity.Order;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.CartRepo;
import com.example.shopapp.repositories.OrderRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Order ORDER = makePojo(Order.class);
    private static final Order ORDER_1 = makePojo(Order.class);
    private static final OrderDto ORDER_DTO = makePojo(OrderDto.class);
    private static final Cart CART = makePojo(Cart.class);
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

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderMapperImpl orderMapper;
    @Mock
    private CartRepo cartRepo;
    @InjectMocks
    private OrderService orderService;

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    void testGetOrder() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.of(ORDER));
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO);
        var orderDto = orderService.getOrder(1L);
        verify(orderRepo).findById(1L);
        verify(orderMapper).toDto(ORDER);
        assertThat(orderDto).isEqualTo(ORDER_DTO);
    }

    @Test
    void testGetOrdersForCurrentUser() {
        when(orderRepo.findAllByUserId(any(Long.class))).thenReturn(List.of(ORDER));
        when(orderMapper.toDtoList(List.of(ORDER))).thenReturn(List.of(ORDER_DTO));
        var orderDtos = orderService.getOrdersForCurrentUser(1L);
        verify(orderRepo).findAllByUserId(1L);
        verify(orderMapper).toDtoList(List.of(ORDER));
        assertThat(orderDtos).isEqualTo(List.of(ORDER_DTO));
    }

    @Test
    void testAddOrder() {
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(ORDER);
        when(cartRepo.findFirstByUserIdOrderByIdDesc(any(Long.class))).thenReturn(CART);
        when(orderRepo.save(any(Order.class))).thenReturn(ORDER_1);
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO);
        var orderDto = orderService.addOrder(ORDER_DTO, USER);
        verify(orderMapper).toEntity(ORDER_DTO);
        verify(cartRepo).findFirstByUserIdOrderByIdDesc(USER.getId());
        verify(orderRepo).save(ORDER);
        verify(orderMapper).toDto(ORDER_1);
        assertThat(orderDto).isEqualTo(ORDER_DTO);
    }

    @Test
    void testRemoveOrder() {
        when(orderRepo.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepo).deleteById(any(Long.class));
        orderService.removeOrder(1L);
        verify(orderRepo).deleteById(1L);
    }

    @Test
    void testOrderInProgress() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(ORDER));
        when(orderRepo.save(any(Order.class))).thenReturn(ORDER);
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO_IN_PROGRESS);
        var orderDto = orderService.orderInProgress(1L);
        verify(orderRepo).findById(1L);
        verify(orderRepo).save(ORDER);
        verify(orderMapper).toDto(ORDER);
        assertEquals(Order.OrderStatus.IN_PROGRESS, orderDto.getOrderStatus());
    }

    @Test
    void testOrderShipped() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(ORDER));
        when(orderRepo.save(any(Order.class))).thenReturn(ORDER);
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO_SHIPPED);
        var orderDto = orderService.orderInProgress(1L);
        verify(orderRepo).findById(1L);
        verify(orderRepo).save(ORDER);
        verify(orderMapper).toDto(ORDER);
        assertEquals(Order.OrderStatus.SHIPPED, orderDto.getOrderStatus());
    }

    @Test
    void testOrderDelivered() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(ORDER));
        when(orderRepo.save(any(Order.class))).thenReturn(ORDER);
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO_DELIVERED);
        var orderDto = orderService.orderInProgress(1L);
        verify(orderRepo).findById(1L);
        verify(orderRepo).save(ORDER);
        verify(orderMapper).toDto(ORDER);
        assertEquals(Order.OrderStatus.DELIVERED, orderDto.getOrderStatus());
    }

    @Test
    void testOrderCanceled() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(ORDER));
        when(orderRepo.save(any(Order.class))).thenReturn(ORDER);
        when(orderMapper.toDto(any(Order.class))).thenReturn(ORDER_DTO_CANCELED);
        var orderDto = orderService.orderInProgress(1L);
        verify(orderRepo).findById(1L);
        verify(orderRepo).save(ORDER);
        verify(orderMapper).toDto(ORDER);
        assertEquals(Order.OrderStatus.CANCELED, orderDto.getOrderStatus());
    }
}
