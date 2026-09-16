package com.WD09.BeautySaloonSystem.repository;

import com.WD09.BeautySaloonSystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String newEmail);
}
