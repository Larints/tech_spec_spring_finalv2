package com.spec.subscriptions.mapper;

import com.spec.subscriptions.dto.request.CreateSubscriptionRequest;
import com.spec.subscriptions.dto.response.SubscriptionDto;
import com.spec.subscriptions.dto.response.SubscriptionWithUserDto;
import com.spec.subscriptions.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {


    Subscription toEntity(CreateSubscriptionRequest request);

    SubscriptionDto toDto(Subscription subscription);

    SubscriptionWithUserDto toWithUserDto(Subscription subscription);

}
