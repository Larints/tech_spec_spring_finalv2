package com.spec.subscriptions.service.impl;

import com.spec.subscriptions.dto.response.SubscriptionDto;
import com.spec.subscriptions.dto.response.TopSubscriptionDto;
import com.spec.subscriptions.dto.response.TopSubscriptionProjection;
import com.spec.subscriptions.exceptions.UserNotFoundException;
import com.spec.subscriptions.mapper.SubscriptionMapper;
import com.spec.subscriptions.model.Subscription;
import com.spec.subscriptions.model.User;
import com.spec.subscriptions.repository.SubscriptionRepository;
import com.spec.subscriptions.repository.UserRepository;
import com.spec.subscriptions.service.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Transactional
    @Override
    public SubscriptionDto addSubscriptionToUser(Long userId, String serviceName) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> {
                    log.warn("User not found when trying to add subscription (FOR UPDATE): id={}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        boolean alreadyExists = user.getSubscriptions().stream()
                .anyMatch(s -> s.getServiceName().equalsIgnoreCase(serviceName));

        if (alreadyExists) {
            throw new IllegalArgumentException("User already subscribed to " + serviceName);
        }

        Subscription subscription = new Subscription();
        subscription.setServiceName(serviceName);
        subscription.setUser(user);

        user.getSubscriptions().add(subscription);
        userRepository.save(user);


        Subscription savedSubscription = user.getSubscriptions().stream()
                .filter(s -> serviceName.equalsIgnoreCase(s.getServiceName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Subscription was not saved"));

        log.info("Subscription added successfully: userId={}, service={}", userId, serviceName);
        return subscriptionMapper.toDto(savedSubscription);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SubscriptionDto> getUserSubscriptions(Long userId, Pageable pageable) {

        if (!userRepository.existsById(userId)) {
            log.warn("User not found for fetching paged subscriptions: id={}", userId);
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        Page<Subscription> page = subscriptionRepository.findByUserId(userId, pageable);
        log.info("Paged subscriptions fetched: userId={}, page={}, size={}, total={}",
                userId, page.getNumber(), page.getSize(), page.getTotalElements());

        return page.map(subscriptionMapper::toDto);
    }

    @Transactional
    @Override
    public SubscriptionDto deleteSubscription(Long userId, Long subscriptionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for deleting subscription: id={}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        Subscription toRemove = user.getSubscriptions().stream()
                .filter(sub -> sub.getId().equals(subscriptionId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Subscription not found for user: subscriptionId={}, userId={}", subscriptionId, userId);
                    return new RuntimeException("Subscription not found");
                });

        user.getSubscriptions().remove(toRemove);
        userRepository.save(user);
        log.info("Subscription deleted via user entity: id={}, userId={}", subscriptionId, userId);

        return subscriptionMapper.toDto(toRemove);
    }


    @Transactional(readOnly = true)
    @Override
    public List<TopSubscriptionDto> getTopSubscriptions() {
        Pageable top3 = PageRequest.of(0, 3);
        List<TopSubscriptionProjection> results = subscriptionRepository.findTopSubscriptions(top3);

        List<TopSubscriptionDto> dtos = results.stream()
                .map(p -> new TopSubscriptionDto(p.getServiceName(), p.getCount()))
                .toList();

        log.info("Top 3 subscriptions fetched: {}", dtos);
        return dtos;
    }
}
