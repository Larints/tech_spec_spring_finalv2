package com.spec.subscriptions.controller.controller;

import com.spec.subscriptions.controller.openapi.AnalyticsApi;
import com.spec.subscriptions.dto.response.TopSubscriptionDto;
import com.spec.subscriptions.service.SubscriptionsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsController implements AnalyticsApi {

    private final SubscriptionsService subscriptionsService;

    @Override
    public ResponseEntity<List<TopSubscriptionDto>> getTopSubscriptions() {
        return ResponseEntity.ok(subscriptionsService.getTopSubscriptions());
    }
}
