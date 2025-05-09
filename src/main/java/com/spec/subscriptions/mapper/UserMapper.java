package com.spec.subscriptions.mapper;

import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;
import com.spec.subscriptions.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = SubscriptionMapper.class)
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    UserDto toDto(User user);

    UserShortDto toShortDto(User user);
}
