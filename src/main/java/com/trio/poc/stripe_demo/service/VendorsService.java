package com.trio.poc.stripe_demo.service;

import com.stripe.model.Account;
import com.trio.poc.stripe_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorsService {

    @Autowired
    private UserRepository userRepository;

    public void handleAccountUpdated(Account stripeAccount) {
        var stripeAccountId = stripeAccount.getId();
        var vendor = userRepository.findByStripeID(stripeAccountId)
                .orElseThrow();

        vendor.setCompletedOnboarding(stripeAccount.getChargesEnabled());
    }
}
