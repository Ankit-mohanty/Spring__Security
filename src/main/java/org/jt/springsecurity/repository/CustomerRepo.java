package org.jt.springsecurity.repository;

import org.jt.springsecurity.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
	Optional<Customer> findByUsername(String username);
}
