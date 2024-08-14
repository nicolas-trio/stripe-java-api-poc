package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
