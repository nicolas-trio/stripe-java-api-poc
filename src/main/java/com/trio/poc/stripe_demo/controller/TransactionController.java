package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.trio.poc.stripe_demo.controller.data.NewTransactionRequest;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.Transaction;
import com.trio.poc.stripe_demo.repository.TransactionRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StripeService stripeService;


    @PostMapping("/checkout")
    public Map<String, Object> checkoutWithFees(@RequestBody Map<String, Object> request) throws StripeException {
        //get the vendor
        var sellerId = Long.parseLong(request.get("sellerId").toString());
        var seller = userRepository.findById(sellerId)
                .orElseThrow();
        var connectedAccountId = seller.getStripeID();

        //create the product
        var productName = request.get("productName").toString();
        var productId = stripeService.createProduct(connectedAccountId, productName);

        //create the price for the product
        var amount = Long.parseLong(request.get("amount").toString());
        var priceId = stripeService.createPrice(connectedAccountId, productId,amount);

        var applicationFee = Double.valueOf(amount * 0.3).longValue();

        var checkoutLink = stripeService.getSinglePaymentCheckoutLink(applicationFee, priceId, connectedAccountId);

        Map<String, Object> response = new HashMap<>();
        response.put("checkoutUrl", checkoutLink);
        return response;
    }

    @PostMapping("/subscription")
    public Map<String, Object> subscription(@RequestBody Map<String, Object> request) throws StripeException {
        //get the vendor
        var sellerId = Long.parseLong(request.get("sellerId").toString());
        var seller = userRepository.findById(sellerId)
                .orElseThrow();
        var connectedAccountId = seller.getStripeID();

        //create the product
        var plan = request.get("plan").toString();
        var productId = stripeService.createProduct(connectedAccountId, plan + " subscription");

        //create the price for the product
        var amount = getAmount(plan);
        var priceId = stripeService.createSubscriptionPrice(connectedAccountId, productId, amount, PriceCreateParams.Recurring.Interval.MONTH);

        var checkoutLink = stripeService.getSubscriptionCheckoutLink(getFee(plan), priceId, connectedAccountId);

        Map<String, Object> response = new HashMap<>();
        response.put("checkoutUrl", checkoutLink);
        return response;
    }

    private Long getAmount(String plan){
        return switch (plan){
            case "basic" ->  1000L;
            case "premium" -> 2500L;
            default -> throw new IllegalStateException("Unexpected value: " + plan);
        };
    }

    private BigDecimal getFee(String plan){
        return switch (plan){
            case "basic", "premium" ->  BigDecimal.TEN ;
            default -> throw new IllegalStateException("Unexpected value: " + plan);
        };
    }
}
