package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.Product;
import com.trio.poc.stripe_demo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByStripeID(String stripeID);
}
