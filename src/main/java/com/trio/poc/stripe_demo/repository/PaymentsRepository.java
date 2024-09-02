package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface  PaymentsRepository extends CrudRepository<Payment, Long> {
}
