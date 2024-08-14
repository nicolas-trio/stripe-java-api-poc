package com.trio.poc.stripe_demo.controller;

import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SellerControllerTests {

    @Autowired
    UserRepository userRepository;

    private static final String API_ROOT
            = "http://localhost:8080/api/sellers";

    private User createAndSaveUnConnectedUser(){
        return userRepository.save(new User("some-unconnected-user"));
    }

    @Test
    public void whenGetById_thenOK() {
        var id = 1;
        Response response = RestAssured.get(API_ROOT + "/"+ id);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1L, response.as(User.class).getId());
    }

    @Test
    public void whenConnectingWithStripe_thenOK(){
        var newUser = createAndSaveUnConnectedUser();

        Map<String,Object> body = new HashMap<>();
        body.put("stripeUserId", "some-stripe-id");

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post(API_ROOT + "/" + newUser.getId() + "/connect");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("some-stripe-id", response.as(User.class).getStripeID());
    }

    @Test
    public void whenGetNotExistProductById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
