package com.trio.poc.stripe_demo.controller;

import com.trio.poc.stripe_demo.model.Transaction;
import com.trio.poc.stripe_demo.repository.TransactionRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    private Transaction createAndSaveTransaction(double amount, double fee){
        //we know this 2 users exists
        var seller = userRepository.findById(1L).orElseThrow();
        var buyer = userRepository.findById(2L).orElseThrow();

        var transaction = new Transaction(seller, buyer, amount, fee);
        return transactionRepository.save(transaction);
    }

    private Transaction createTransaction(double amount, double fee){
        //we know this 2 users exists
        var seller = userRepository.findById(1L).orElseThrow();
        var buyer = userRepository.findById(2L).orElseThrow();

        return new Transaction(seller, buyer, amount, fee);
    }

    @Test
    public void whenGetAllTransactions_thenOK() {
        createAndSaveTransaction(100, 10);
        createAndSaveTransaction(150, 15);
        createAndSaveTransaction(200, 20);

        Response response = RestAssured.get(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(3, response.as(List.class).size());
    }


    @Test
    public void whenPostNewTransactions_thenOK() {
        var newTransaction = createTransaction(100, 10);

        Map<String,Object> body = new HashMap<>();
        body.put("sellerId", newTransaction.getSeller().getId());
        body.put("buyerId", newTransaction.getBuyer().getId());
        body.put("amount", newTransaction.getAmount());
        body.put("fee", newTransaction.getMarketplaceFee());

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post(API_ROOT);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(100, response.as(Transaction.class).getAmount());
        assertEquals(10, response.as(Transaction.class).getMarketplaceFee());

    }

}
