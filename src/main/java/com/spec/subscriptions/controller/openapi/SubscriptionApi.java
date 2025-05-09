package com.spec.subscriptions.controller.openapi;

import com.spec.subscriptions.dto.request.CreateSubscriptionRequest;
import com.spec.subscriptions.dto.response.SubscriptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subscriptions", description = "Управление подписками пользователей")
@RequestMapping("/users/{userId}/subscriptions")
public interface SubscriptionApi {

    @Operation(summary = "Добавить подписку пользователю")
    @PostMapping
    ResponseEntity<SubscriptionDto> addSubscription(@PathVariable Long userId,
                                                    @RequestBody @Valid CreateSubscriptionRequest request);

    @Operation(summary = "Получить подписки пользователя (страница)")
    @GetMapping
    ResponseEntity<Page<SubscriptionDto>> getUserSubscriptions(@PathVariable Long userId,
                                                               @Parameter(hidden = true)
                                                               Pageable pageable);

    @Operation(summary = "Удалить подписку пользователя")
    @DeleteMapping("/{subscriptionId}")
    ResponseEntity<SubscriptionDto> deleteSubscription(@PathVariable Long userId,
                                                       @PathVariable Long subscriptionId);

}
