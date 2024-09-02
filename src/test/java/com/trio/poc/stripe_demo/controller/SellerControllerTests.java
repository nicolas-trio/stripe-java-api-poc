package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
public class SellerControllerTests {

    @MockBean
    StripeService stripeService;

    @Autowired
    UserRepository userRepository;

    private static final String API_ROOT
            = "http://localhost:8080/api/sellers";

    private User createAndSaveUnConnectedUser(){
        return userRepository.save(new User("some-unconnected-user", "some@example.com"));
    }

    @Test
    public void whenGetById_thenOK() {
        var id = 1;
        Response response = RestAssured.get(API_ROOT + "/"+ id);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1L, response.as(User.class).getId());
    }

    @Test
    public void whenConnectingWithStripe_thenOK() throws StripeException {
        Mockito.when(stripeService.createStripeConnectedAccount(ArgumentMatchers.anyString()))
                .thenReturn("some-stripe-account-id");
        Mockito.when(stripeService.getAccountLinkUrl(ArgumentMatchers.anyString()))
                .thenReturn("some-url-to-link-stripe-account");

        var newUser = createAndSaveUnConnectedUser();

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROOT + "/" + newUser.getId() + "/link");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        var responseMap = response.as(Map.class);

        assertEquals("some-stripe-account-id", responseMap.get("stripeId"));
        assertEquals("some-url-to-link-stripe-account", responseMap.get("url"));
    }

    @Test
    public void whenGetNotExistSellerById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
