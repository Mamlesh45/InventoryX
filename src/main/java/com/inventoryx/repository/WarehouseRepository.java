package com.inventoryx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}