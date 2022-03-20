package com.example.shopapp.validators;

import com.example.shopapp.exceptions.IllegalUserException;
import com.example.shopapp.models.entity.Role;
import com.example.shopapp.models.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserValidatorTest {

    private UserValidator userValidator;

    private static final Long REQUESTED_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "1,ADMIN",
            "1,USER",
            "2,ADMIN"
    })
    void testValidateSingleUser(Long id, String role) {
        var token = mock(PreAuthenticatedAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(User.builder()
                .id(id)
                .roles(List.of(Role.builder().name(role).build()))
                .build());
        assertDoesNotThrow(() -> userValidator.validateSingleUser(REQUESTED_USER_ID, token));
    }

    @ParameterizedTest
    @CsvSource({
            "2,USER",
            "2,STAFF"
    })
    void throwsException_testValidateSingleUser(Long id, String role) {
       var token = mock(PreAuthenticatedAuthenticationToken.class);
       when(token.getPrincipal()).thenReturn(User.builder()
               .id(id)
               .roles(List.of(Role.builder().name(role).build()))
               .build());
       assertThrows(IllegalUserException.class, () -> userValidator.validateSingleUser(REQUESTED_USER_ID, token));
    }

}