package com.spec.subscriptions.repository;

import com.spec.subscriptions.dto.response.TopSubscriptionProjection;
import com.spec.subscriptions.model.Subscription;
import com.spec.subscriptions.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findByUserId(Long userId, Pageable pageable);

    Optional<Subscription> findByIdAndUserId(Long id, Long userId);

    @Query("""
    SELECT s.serviceName AS serviceName, COUNT(s.id) AS count
    FROM Subscription s
    GROUP BY s.serviceName
    ORDER BY COUNT(s.id) DESC
    """)
    List<TopSubscriptionProjection> findTopSubscriptions(Pageable pageable);


}
