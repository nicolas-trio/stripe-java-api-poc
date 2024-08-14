package com.trio.poc.stripe_demo.controller;

import com.trio.poc.stripe_demo.controller.data.ConnectRequest;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
    }

    //TODO: prior to this, we need to fetch the stripe data from the stripe api
    @PostMapping("/{id}/connect")
    public User connectSellerStripeAccount(@PathVariable Long id, @RequestBody ConnectRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        user.setStripeID(request.getStripeUserId());
        return userRepository.save(user);
    }
}
