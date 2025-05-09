package com.spec.subscriptions.service;


import com.spec.subscriptions.dto.response.SubscriptionDto;

import com.spec.subscriptions.dto.response.TopSubscriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SubscriptionsService {

    SubscriptionDto addSubscriptionToUser(Long userId, String serviceName);

    Page<SubscriptionDto> getUserSubscriptions(Long userId, Pageable pageable);

    SubscriptionDto deleteSubscription(Long userId, Long subscriptionId);

    public List<TopSubscriptionDto> getTopSubscriptions();
}
