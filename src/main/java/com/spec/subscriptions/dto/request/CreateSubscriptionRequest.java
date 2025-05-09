package com.spec.subscriptions.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSubscriptionRequest(

        @NotBlank(message = "Имя сервиса не может быть пустым")
        String serviceName) {
}
