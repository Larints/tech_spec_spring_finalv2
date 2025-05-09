package com.spec.subscriptions.controller.openapi;

import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Управление пользователями")
@RequestMapping("/users")
public interface UserApi {

    @Operation(summary = "Создать пользователя")
    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest request);

    @Operation(summary = "Получить информацию о пользователе")
    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id);

    @Operation(summary = "Обновить данные пользователя")
    @PutMapping("/{id}")
    ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                       @RequestBody @Valid UpdateUserRequest request);

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    ResponseEntity<UserShortDto> deleteUser(@PathVariable Long id);

}
