package com.inventoryx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
}
