package com.example.shopapp.models.dto;

import com.example.shopapp.models.entity.Order;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
@Setter
public class OrderDto {
    String firstName;
    String lastName;
    String address;
    String city;
    String postalCode;
    Order.OrderStatus orderStatus;
    CartDto cartDto;
}
