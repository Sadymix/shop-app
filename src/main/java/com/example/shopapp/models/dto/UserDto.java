package com.example.shopapp.models.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserDto {
    String username;
    String password;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
    boolean enabled;
    List<String> roles;
}
