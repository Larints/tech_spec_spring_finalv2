package com.spec.subscriptions.dto.response;

public record SubscriptionWithUserDto(Long id, String serviceName, UserShortDto user) {
}
