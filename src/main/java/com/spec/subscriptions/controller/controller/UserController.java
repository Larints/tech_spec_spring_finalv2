package com.spec.subscriptions.controller.controller;

import com.spec.subscriptions.controller.openapi.UserApi;
import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;
import com.spec.subscriptions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;


    @Override
    public ResponseEntity<UserDto> createUser(CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Override
    public ResponseEntity<UserDto> getUser(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Override
    public ResponseEntity<UserShortDto> deleteUser(Long id) {
       return ResponseEntity.ok(userService.deleteUser(id));
    }
}
