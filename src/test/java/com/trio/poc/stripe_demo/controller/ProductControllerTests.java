package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.trio.poc.stripe_demo.model.Product;
import com.trio.poc.stripe_demo.model.User;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    private static final String API_ROOT
            = "http://localhost:8080/api/products";

    @MockBean
    StripeService stripeService;

    @Autowired
    UserRepository userRepository;

    private Product createRandomProduct() {
        //we know user with id = 1 exists
        User seller = userRepository.findById(1L).orElseThrow();
        return new Product(randomAlphabetic(10), seller, 10d);
    }

    private String postProduct(Product product) throws StripeException {

        Mockito.when(stripeService.createProduct(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-product-id");
        Mockito.when(stripeService.createPrice(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("some-price-id");

        Map<String,Object> body = new HashMap<>();
        body.put("name", product.getName());
        body.put("sellerId", product.getSeller().getId());
        body.put("price", product.getPrice());

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }


    @Test
    public void whenGetAllProducts_thenOK() {
        Response response = RestAssured.get(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetProductByTitle_thenOK() throws StripeException {
        Product product = createRandomProduct();
        postProduct(product);
        Response response = RestAssured.get(
                API_ROOT + "/name/" + product.getName());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertFalse(response.as(List.class).isEmpty());
    }
    @Test
    public void whenGetCreatedProductById_thenOK() throws StripeException {
        Product product = createRandomProduct();
        String location = postProduct(product);
        Response response = RestAssured.get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(product.getName(), response.jsonPath().get("name"));
    }

    @Test
    public void whenGetNotExistProductById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
