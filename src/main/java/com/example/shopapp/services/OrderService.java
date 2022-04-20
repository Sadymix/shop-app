package com.example.shopapp.services;

import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.mappers.OrderMapperImpl;
import com.example.shopapp.models.dto.OrderDto;
import com.example.shopapp.models.entity.Cart;
import com.example.shopapp.models.entity.Order;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.CartRepo;
import com.example.shopapp.repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapperImpl orderMapper;
    private final CartRepo cartRepo;

    public OrderDto getOrder(Long id) {
        return orderMapper.toDto(getOrderById(id));
    }

    public List<OrderDto> getOrdersForCurrentUser(Long id) {
        var orders = orderRepo.findAllByUserId(id);
        return orderMapper.toDtoList(orders);
    }

    public OrderDto addOrder(OrderDto orderDto, User user) {
        var order = orderMapper.toEntity(orderDto);
        order.setOrderStatus(Order.OrderStatus.AWAITING_PAYMENT);
        order.setUser(user);
        order.setCart(getLastCart(user.getId()));
        var orderSave = orderRepo.save(order);
        return orderMapper.toDto(orderSave);
    }

    public void removeOrder(Long id) {
        if (!orderRepo.existsById(id)) {
            throw new ResourceNotFoundException("Order with this id not found");
        }
        orderRepo.deleteById(id);
    }

    @Transactional
    public OrderDto orderInProgress(Long id) {
        return setOrderStatus(id, Order.OrderStatus.IN_PROGRESS);
    }

    @Transactional
    public OrderDto orderShipped(Long id) {
        return setOrderStatus(id, Order.OrderStatus.SHIPPED);
    }

    @Transactional
    public OrderDto orderDelivered(Long id) {
        return setOrderStatus(id, Order.OrderStatus.DELIVERED);
    }

    @Transactional
    public OrderDto orderCanceled(Long id) {
        return setOrderStatus(id, Order.OrderStatus.CANCELED);
    }

    private Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order with this id not found"));
    }

    private Cart getLastCart(Long id) {
        var lastCart = cartRepo.findFirstByUserIdOrderByIdDesc(id);
        if(lastCart==null) {
            throw new ResourceNotFoundException("Cart with this user id not exist in database.");
        }
        return lastCart;
    }

    private OrderDto setOrderStatus(Long id, Order.OrderStatus status) {
        var order = getOrderById(id);
        order.setOrderStatus(status);
        orderRepo.save(order);
        return orderMapper.toDto(order);
    }

}
