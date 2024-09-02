package com.trio.poc.stripe_demo.controller;

import com.stripe.exception.StripeException;
import com.trio.poc.stripe_demo.controller.data.NewProductRequest;
import com.trio.poc.stripe_demo.exceptions.RecordNotFoundException;
import com.trio.poc.stripe_demo.model.Product;
import com.trio.poc.stripe_demo.repository.ProductRepository;
import com.trio.poc.stripe_demo.repository.UserRepository;
import com.trio.poc.stripe_demo.stripe.StripeService;
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

    @Autowired
    private StripeService stripeService;

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
    public Product create(@RequestBody NewProductRequest request) throws StripeException {
        logger.info("Creating new product with name: {}, sellerID: {}, price: {}", request.getName(), request.getSellerId(), request.getPrice());

        var seller = userRepository.findById(request.getSellerId())
                .orElseThrow(RecordNotFoundException::new);

        var newProduct = new Product(request.getName(), seller, request.getPrice());

        //create the product in stripe
        var productStripeId = stripeService.createProduct(seller.getStripeID(), newProduct.getName());
        newProduct.setStripeId(productStripeId);

        //create the price (for this we assume a single price for each product)
        var priceStripeId = stripeService.createPrice(seller.getStripeID(), productStripeId, request.getPrice());
        newProduct.setPriceStripeId(priceStripeId);

        return productRepository.save(newProduct);
    }
}
