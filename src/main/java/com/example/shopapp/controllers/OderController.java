package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.OrderDto;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class OderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @GetMapping("/user")
    public List<OrderDto> getOrdersForCurrentUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            return orderService.getOrdersForCurrentUser(user.getId());
        }
        throw new IllegalStateException("The token does not contain authorized user data.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @PostMapping()
    public OrderDto addOrder(@Valid @RequestBody OrderDto orderDto, Authentication authentication){
        if (authentication.getPrincipal() instanceof User user) {
            return orderService.addOrder(orderDto, user);
        }
        throw new IllegalStateException("The token does not contain authorized user data.");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.removeOrder(id);
        return "Deleted reservation with id: " + id;
    }

    @PutMapping("/{id}/in_progress")
    public OrderDto orderInProgress(@PathVariable Long id) {
        return orderService.orderInProgress(id);
    }

    @PutMapping("/{id}/shipped")
    public OrderDto orderShipped(@PathVariable Long id) {
        return orderService.orderShipped(id);
    }

    @PutMapping("/{id}/delivered")
    public OrderDto orderDelivered(@PathVariable Long id) {
        return orderService.orderDelivered(id);
    }

    @PutMapping("/{id}/canceled")
    public OrderDto orderCanceled(@PathVariable Long id) {
        return orderService.orderCanceled(id);
    }
}
