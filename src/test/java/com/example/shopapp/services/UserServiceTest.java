package com.example.shopapp.services;

import com.example.shopapp.mappers.UserMapperImpl;
import com.example.shopapp.models.dto.UserDto;
import com.example.shopapp.models.entity.Role;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.RoleRepo;
import com.example.shopapp.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final UserDto USER_DTO = makePojo(UserDto.class);
    private static final User USER = makePojo(User.class);
    private static final Role ROLE = makePojo(Role.class);
    @Spy
    private UserMapperImpl userMapper = new UserMapperImpl();
    @Mock
    private UserRepo userRepo;
    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private RoleRepo roleRepo;
    @Spy
    private TokenStore tokenStore = new InMemoryTokenStore();
    @InjectMocks
    private UserService userService;

    @Test
    void testGetUser() {
        when(userRepo.findById(USER.getId())).thenReturn(Optional.of(USER));
        var getUser = userService.getUser(USER.getId());
        verify(userRepo).findById(USER.getId());
        assertThat(getUser).isEqualTo(userMapper.toDto(USER));
    }

    @Test
    void testAddUser() {
        when(roleRepo.findByName(any(String.class))).thenReturn(ROLE);
        when(userRepo.save(any(User.class))).thenReturn(USER);
        var testProductDto = userService.addUser(USER_DTO);
        verify(userRepo).save(any(User.class));
        assertThat(testProductDto.getUsername()).isEqualTo(USER.getUsername());
        assertThat(testProductDto.getRoles())
                .isEqualTo(USER.getRoles().stream().map(Role::getName).toList());
    }

    @Test
    void testActivateUser() {
        when(userRepo.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepo.save(any(User.class))).thenReturn(USER);
        var activateUser = userService.activateUser(USER.getId());
        verify(userRepo).findById(USER.getId());
        verify(userRepo).save(any(User.class));
        assertThat(activateUser.isEnabled()).isEqualTo(USER.isEnabled());
    }

    @Test
    void testDeactivateUser() {
        when(userRepo.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepo.save(any(User.class))).thenReturn(USER);
        var activateUser = userService.activateUser(USER.getId());
        verify(userRepo).findById(USER.getId());
        verify(userRepo).save(any(User.class));
        assertThat(activateUser.isEnabled()).isEqualTo(USER.isEnabled());
    }

    @Test
    void testSetUserRoles() {
        when(userRepo.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(roleRepo.findByName(any(String.class))).thenReturn(ROLE);
        when(userRepo.save(any(User.class))).thenReturn(USER);
        var roleUser = userService.setUserRoles(USER.getId(), List.of("ADMIN"));
        verify(roleRepo).findByName(any(String.class));
        verify(userRepo).save(any(User.class));
        assertThat(roleUser.getRoles())
                .isEqualTo(USER.getRoles().stream().map(Role::getName).toList());
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepo.findByUsername(USER.getUsername())).thenReturn(USER);
        var user = userService.loadUserByUsername(USER.getUsername());
        verify(userRepo).findByUsername(USER.getUsername());
        assertThat(user).isEqualTo(USER);
    }

    @Test
    void testLoadByUsernameExceptionOccurrence() {
        when(userRepo.findByUsername(USER.getUsername())).thenReturn(USER);
        var user = userService.loadUserByUsername(USER.getUsername());
        var exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(USER.getUsername()+ "1"));
        assertTrue(exception.getMessage().contains(USER.getUsername()+ "1"));
    }

    @Nested
    class LoadUserDetailsTest {
        @Mock
        private Authentication authentication;
        @Mock
        private UserDetails userDetails;
        @Mock
        private PreAuthenticatedAuthenticationToken token;

        @BeforeEach
        void setUp() {
            String tokenString = "QWERTY";
            var oAuth2Authentication = mock(OAuth2Authentication.class);
            when(token.getPrincipal())
                    .thenReturn(tokenString);
            when(tokenStore.readAuthentication(tokenString))
                    .thenReturn(oAuth2Authentication);
            when(oAuth2Authentication.getUserAuthentication())
                    .thenReturn(authentication);
        }

        @Test
        void testLoadUserDetails() {
            when(authentication.isAuthenticated())
                    .thenReturn(true);
            when(authentication.getPrincipal())
                    .thenReturn(userDetails);
            var userDetailsService = userService.loadUserDetails(token);
            assertEquals(userDetails, userDetailsService);
        }

        @Test
        void testLoadUserDetails_Unauthenticated() {
            when(authentication.isAuthenticated()).thenReturn(false);
            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserDetails(token));
        }
    }
}