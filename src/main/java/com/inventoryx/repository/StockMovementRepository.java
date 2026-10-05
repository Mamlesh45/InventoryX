package com.inventoryx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventoryx.entity.StockMovement;
import com.inventoryx.entity.StockMovementType;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    long countByType(StockMovementType type);

    @Query("""
            SELECT COALESCE(SUM(s.quantity), 0)
            FROM StockMovement s
            WHERE s.type = :type
            """)
    Long calculateTotalQuantityByType(
            @Param("type") StockMovementType type
    );
}