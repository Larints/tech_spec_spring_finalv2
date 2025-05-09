package com.spec.subscriptions.service;

import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;


public interface UserService {

    UserDto createUser(CreateUserRequest request);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UpdateUserRequest request);

    UserShortDto deleteUser(Long id);
}
