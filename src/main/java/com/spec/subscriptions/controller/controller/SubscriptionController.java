package com.spec.subscriptions.controller.controller;

import com.spec.subscriptions.controller.openapi.SubscriptionApi;
import com.spec.subscriptions.dto.request.CreateSubscriptionRequest;
import com.spec.subscriptions.dto.response.SubscriptionDto;
import com.spec.subscriptions.service.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionApi {

    private final SubscriptionsService subscriptionsService;
    @Override
    public ResponseEntity<SubscriptionDto> addSubscription(Long userId,
                                                           CreateSubscriptionRequest request) {
        SubscriptionDto dto = subscriptionsService.addSubscriptionToUser(userId, request.serviceName());
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Page<SubscriptionDto>> getUserSubscriptions(Long userId, Pageable pageable) {
        return ResponseEntity.ok(subscriptionsService.getUserSubscriptions(userId, pageable));
    }

    @Override
    public ResponseEntity<SubscriptionDto> deleteSubscription(Long userId, Long subscriptionId) {
        return ResponseEntity.ok(subscriptionsService.deleteSubscription(userId, subscriptionId));
    }
}
