package com.WD09.BeautySaloonSystem.repository;


import com.WD09.BeautySaloonSystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByQuantityGreaterThan(int quantity);
}