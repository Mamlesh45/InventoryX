package com.inventoryx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    long countByQuantityLessThanEqual(Integer quantity);

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}