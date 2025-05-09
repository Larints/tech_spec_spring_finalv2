package com.spec.subscriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spec.subscriptions.controller.controller.UserController;
import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;
import com.spec.subscriptions.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private UserDto userDto;
    private UserShortDto shortDto;

    @BeforeEach
    void setUp() {
        createRequest = new CreateUserRequest("John", "john@example.com");
        updateRequest = new UpdateUserRequest("Johnny", "johnny@example.com");
        userDto = new UserDto(1L, "John", "john@example.com", new ArrayList<>());
        shortDto = new UserShortDto(1L, "John", "john@example.com");
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        given(userService.createUser(any())).willReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void getUser_shouldReturnUserById() throws Exception {
        given(userService.getUserById(1L)).willReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        given(userService.updateUser(eq(1L), any())).willReturn(userDto);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void deleteUser_shouldReturnShortUserDto() throws Exception {
        given(userService.deleteUser(1L)).willReturn(shortDto);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shortDto.id()))
                .andExpect(jsonPath("$.name").value(shortDto.name()));
    }
}
