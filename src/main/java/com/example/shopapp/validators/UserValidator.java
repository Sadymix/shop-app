package com.example.shopapp.validators;

import com.example.shopapp.exceptions.IllegalUserException;
import com.example.shopapp.models.entity.Role;
import com.example.shopapp.models.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class UserValidator {

    public void validateSingleUser(Long id, Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            var hasAdmin = user.getRoles().stream()
                    .map(Role::getName)
                    .anyMatch("ADMIN"::equals);
            if (!(Objects.equals(user.getId(), id) || hasAdmin)) {
                throw new IllegalUserException("The token doesn't have the permission to access single user.");
            }
        }
    }
}
