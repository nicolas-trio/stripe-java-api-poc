package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionsRepository extends CrudRepository<Subscription, Long> {
}
