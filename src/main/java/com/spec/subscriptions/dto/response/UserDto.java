package com.spec.subscriptions.dto.response;

import java.util.List;

public record UserDto(Long id, String name, String email, List<SubscriptionDto> subscriptions) {
}
