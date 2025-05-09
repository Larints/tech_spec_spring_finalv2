package com.spec.subscriptions.service.impl;

import com.spec.subscriptions.dto.request.CreateUserRequest;
import com.spec.subscriptions.dto.request.UpdateUserRequest;
import com.spec.subscriptions.dto.response.UserDto;
import com.spec.subscriptions.dto.response.UserShortDto;
import com.spec.subscriptions.exceptions.UserNotFoundException;
import com.spec.subscriptions.mapper.UserMapper;
import com.spec.subscriptions.model.User;
import com.spec.subscriptions.repository.UserRepository;
import com.spec.subscriptions.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        log.info("User created: id={}, email={}", saved.getId(), saved.getEmail());
        return userMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findByIdWithSubscriptions(id)
                .map(user -> {
                    log.info("User fetched: id={}", id);
                    return userMapper.toDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("User not found: id={}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdForUpdate(id)
                .orElseThrow(() -> {
                    log.warn("User not found for update: id={}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
        userMapper.updateUserFromRequest(request, user);
        log.info("User updated: id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    public UserShortDto deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for deletion: id={}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
        userRepository.delete(user);
        log.info("User deleted: id={}", id);
        return userMapper.toShortDto(user);
    }
}
