package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
