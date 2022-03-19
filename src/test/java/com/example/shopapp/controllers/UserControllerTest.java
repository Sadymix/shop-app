package com.example.shopapp.controllers;

import com.example.shopapp.models.dto.UserDto;
import com.example.shopapp.services.UserService;
import com.example.shopapp.validators.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.example.shopapp.utility.PodamUtility.makePojo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private static final UserDto USER_DTO = makePojo(UserDto.class);
    private static final Authentication AUTHENTICATION = makePojo(Authentication.class);
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserValidator userValidator;

    private MockMvc mockMvc;

    UserControllerTest() {
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @SneakyThrows
    @WithMockUser(authorities = "ADMIN")
    @Test
    void testGetUser() {
        when(userService.getUser(1L)).thenReturn(USER_DTO);
        doNothing().when(userValidator).validateSingleUser(1L, AUTHENTICATION);
        mockMvc.perform(get("/api/users/1")
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_DTO.getUsername()))
                .andExpect(jsonPath("$.password").value(USER_DTO.getPassword()))
                .andExpect(jsonPath("$.accountNonExpired").value(USER_DTO.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(USER_DTO.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(USER_DTO.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(USER_DTO.isEnabled()))
                .andExpect(jsonPath("$.roles").value(USER_DTO.getRoles()));
        verify(userService).getUser(1L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testAddUser() {
        when(userService.addUser(USER_DTO)).thenReturn(USER_DTO);
        mockMvc.perform(post("/api/users")
                    .content(objectMapper.writeValueAsString(USER_DTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_DTO.getUsername()))
                .andExpect(jsonPath("$.password").value(USER_DTO.getPassword()))
                .andExpect(jsonPath("$.accountNonExpired").value(USER_DTO.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(USER_DTO.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(USER_DTO.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(USER_DTO.isEnabled()))
                .andExpect(jsonPath("$.roles").value(USER_DTO.getRoles()));
        verify(userService).addUser(USER_DTO);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testActivateUser() {
        when(userService.activateUser(1L)).thenReturn(USER_DTO);
        mockMvc.perform(put("/api/users/1/enable")
                .content(objectMapper.writeValueAsString(USER_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_DTO.getUsername()))
                .andExpect(jsonPath("$.password").value(USER_DTO.getPassword()))
                .andExpect(jsonPath("$.accountNonExpired").value(USER_DTO.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(USER_DTO.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(USER_DTO.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(USER_DTO.isEnabled()))
                .andExpect(jsonPath("$.roles").value(USER_DTO.getRoles()));
        verify(userService).activateUser(1L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testDeactivateUser() {
        when(userService.deactivateUser(2L)).thenReturn(USER_DTO);
        mockMvc.perform(put("/api/users/2/disable")
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_DTO.getUsername()))
                .andExpect(jsonPath("$.password").value(USER_DTO.getPassword()))
                .andExpect(jsonPath("$.accountNonExpired").value(USER_DTO.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(USER_DTO.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(USER_DTO.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(USER_DTO.isEnabled()))
                .andExpect(jsonPath("$.roles").value(USER_DTO.getRoles()));
        verify(userService).deactivateUser(2L);
    }

    @WithMockUser(authorities = "ADMIN")
    @SneakyThrows
    @Test
    void testSetUserRoles() {
        when(userService.setUserRoles(1L, List.of("USER"))).thenReturn(USER_DTO);
        mockMvc.perform(put("/api/users/1/roles")
                        .content(objectMapper.writeValueAsString(List.of("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_DTO.getUsername()))
                .andExpect(jsonPath("$.password").value(USER_DTO.getPassword()))
                .andExpect(jsonPath("$.accountNonExpired").value(USER_DTO.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(USER_DTO.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(USER_DTO.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(USER_DTO.isEnabled()))
                .andExpect(jsonPath("$.roles").value(USER_DTO.getRoles()))
                .andReturn().getResponse().getContentAsString();
        verify(userService).setUserRoles(1L, List.of("USER"));
    }

}