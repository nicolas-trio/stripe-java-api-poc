package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.trio.poc.stripe_demo.model.Transaction;
import com.trio.poc.stripe_demo.repository.TransactionRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TransactionControllerTests {

    private static final String API_ROOT
            = "http://localhost:8080/api/transactions";

    @MockBean
    StripeService stripeService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void whenCheckoutSinglePayment_thenOK() throws StripeException {
        Mockito.when(stripeService.createProduct(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-product-id");
        Mockito.when(stripeService.createPrice(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-price-id");
        Mockito.when(stripeService.getSinglePaymentCheckoutLink(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-checkout-single-payment-link");

        Map<String,Object> body = new HashMap<>();
        body.put("productName", "some-product-name");
        body.put("sellerId", "1");
        body.put("amount", 1000L);

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post(API_ROOT + "/checkout");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var responseMap = response.as(Map.class);
        assertEquals("some-checkout-single-payment-link", responseMap.get("checkoutUrl"));
    }

    @Test
    public void whenCheckoutSubscription_ThenOK() throws StripeException {
        Mockito.when(stripeService.createProduct(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-product-id");
        Mockito.when(stripeService.createSubscriptionPrice(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-price-id");
        Mockito.when(stripeService.getSubscriptionCheckoutLink(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-checkout-subscription-link");

        Map<String,Object> body = new HashMap<>();
        body.put("plan", "basic");
        body.put("sellerId", "1");

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post(API_ROOT + "/subscription");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var responseMap = response.as(Map.class);
        assertEquals("some-checkout-subscription-link", responseMap.get("checkoutUrl"));
    }

}
