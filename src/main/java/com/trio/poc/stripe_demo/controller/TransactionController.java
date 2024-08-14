package com.trio.poc.stripe_demo.controller;

import com.trio.poc.stripe_demo.controller.data.NewTransactionRequest;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.Transaction;
import com.trio.poc.stripe_demo.repository.TransactionRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable findAll() {
        return transactionRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody NewTransactionRequest request) {
        logger.info("Creating new transaction with sellerId: {}, buyerId: {}, amount: {}, fee: {}", request.getSellerId(), request.getBuyerId(), request.getAmount(), request.getFee());

        var seller = userRepository.findById(request.getSellerId())
                .orElseThrow(RecordNotFoundException::new);

        var buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(RecordNotFoundException::new);

        var newTransaction = new Transaction(seller, buyer, request.getAmount(), request.getFee());

        //TODO: call stripe api to create the payment. If success, store in the database

        return transactionRepository.save(newTransaction);
    }
}
