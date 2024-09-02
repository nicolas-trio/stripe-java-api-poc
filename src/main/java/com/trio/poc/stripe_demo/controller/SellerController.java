package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StripeService stripeService;

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
    }

    @GetMapping("/{id}/link")
    public Map<String, String> getAccountLink(@PathVariable Long id) throws StripeException {
        //get the user from our database
        var user = userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        var stripeAccountId = stripeService.createStripeConnectedAccount(user.getEmail());

        user.setStripeID(stripeAccountId);
        userRepository.save(user);

        var accountLinkUrl = stripeService.getAccountLinkUrl(stripeAccountId);

        Map<String, String> response = new HashMap<>();
        response.put("url", accountLinkUrl);
        response.put("stripeId", stripeAccountId);
        return response;
    }
}
