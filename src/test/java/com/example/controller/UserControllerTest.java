package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto user1;
    private UserDto user2;

    @BeforeEach
    void setUp() {
        user1 = new UserDto(1L, "Misha", "misha@mail.com", 25, LocalDateTime.now());
        user2 = new UserDto(2L, "Alex", "alex@mail.com", 30, LocalDateTime.now());
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("Misha"))
                .andExpect(jsonPath("$[1].username").value("Alex"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Misha"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto input = new UserDto(null, "NewUser", "new@mail.com", 22, null);
        UserDto saved = new UserDto(3L, "NewUser", "new@mail.com", 22, LocalDateTime.now());

        when(userService.createUser(any(UserDto.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.username").value("NewUser"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto input = new UserDto(null, "Updated", "updated@mail.com", 28, null);
        UserDto updated = new UserDto(1L, "Updated", "updated@mail.com", 28, LocalDateTime.now());

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Updated"));

        verify(userService, times(1)).updateUser(eq(1L), any(UserDto.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}
