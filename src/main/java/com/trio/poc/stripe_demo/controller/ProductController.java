package com.trio.poc.stripe_demo.controller;

import com.trio.poc.stripe_demo.controller.data.NewProductRequest;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.Product;
import com.trio.poc.stripe_demo.repository.ProductRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/name/{productName}")
    public List findByName(@PathVariable String productName) {
        return productRepository.findByName(productName);
    }

    @GetMapping("/{id}")
    public Product findOne(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody NewProductRequest request) {
        logger.info("Creating new product with name: {}, sellerID: {}, price: {}", request.getName(), request.getSellerId(), request.getPrice());

        var seller = userRepository.findById(request.getSellerId())
                .orElseThrow(RecordNotFoundException::new);

        var newProduct = new Product(request.getName(), seller, request.getPrice());
        return productRepository.save(newProduct);
    }
}
