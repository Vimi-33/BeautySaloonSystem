package com.WD09.BeautySaloonSystem.repository;

import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();
}