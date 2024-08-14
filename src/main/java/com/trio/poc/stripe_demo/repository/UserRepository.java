package com.trio.poc.stripe_demo.repository;

import com.trio.poc.stripe_demo.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}
