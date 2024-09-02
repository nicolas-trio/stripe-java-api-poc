package com.trio.poc.stripe_demo;

import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;

    @Value("${stripe.integration.connected-account-id}")
    private String connectedAccountId;

    @Autowired
    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
        userRepository.save(User.createSeller("seller-1", connectedAccountId, "seller@example.com"));
        userRepository.save(new User("buyer-1", "buyer@example.com"));
    }
}
