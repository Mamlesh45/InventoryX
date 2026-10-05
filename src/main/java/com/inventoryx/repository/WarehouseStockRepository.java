package com.inventoryx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.WarehouseStock;

public interface WarehouseStockRepository
        extends JpaRepository<WarehouseStock, Long> {

    Optional<WarehouseStock> findByWarehouseIdAndProductId(
            Long warehouseId,
            Long productId
    );

    boolean existsByWarehouseIdAndProductId(
            Long warehouseId,
            Long productId
    );
    long countByQuantityLessThanEqual(Integer quantity);
}