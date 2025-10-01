package com.victor.BookFlix.repository;

import com.victor.BookFlix.entity.Subscription;
import com.victor.BookFlix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByUser(User user);
    List<Subscription> findByActive(boolean active);

    long countByActive(boolean active);

}
