package com.trio.poc.stripe_demo.service;

import com.stripe.model.Subscription;
import com.trio.poc.stripe_demo.model.Product;
import com.trio.poc.stripe_demo.repository.SubscriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionsService {

    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    public void handleSubscriptionCreated(Subscription stripeSubscription) {

        var subscribedItem = stripeSubscription.getItems().getData().get(0);

        var newSubscription = new com.trio.poc.stripe_demo.model.Subscription();
        newSubscription.setStripeSubscriptionId(stripeSubscription.getId());
        newSubscription.setClientEmail(stripeSubscription.getCustomerObject().getEmail());
        newSubscription.setProductId(subscribedItem.getPrice().getProduct());
        newSubscription.setPriceId(subscribedItem.getPrice().getId());

        subscriptionsRepository.save(newSubscription);
    }
}
