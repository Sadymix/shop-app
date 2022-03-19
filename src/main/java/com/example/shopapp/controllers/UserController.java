package com.example.shopapp.controllers;


import com.example.shopapp.models.dto.UserDto;
import com.example.shopapp.services.UserService;
import com.example.shopapp.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public UserDto getUser(@Valid @PathVariable Long id, Authentication authentication) {
        userValidator.validateSingleUser(id, authentication);
        return userService.getUser(id);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PutMapping("/{id}/enable")
    public UserDto activateUser(@Valid @PathVariable Long id) {
        return userService.activateUser(id);
    }

    @PutMapping("/{id}/disable")
    public UserDto deactivateUser(@Valid @PathVariable Long  id) {
        return userService.deactivateUser(id);
    }

    @PutMapping("/{id}/roles")
    public UserDto setUserRoles(@PathVariable Long id, @RequestBody List<String> roleStringList) {
        return userService.setUserRoles(id, roleStringList);
    }
}
