package com.inventoryx.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventoryx.dto.WarehouseStockRequestDTO;
import com.inventoryx.dto.WarehouseStockResponseDTO;
import com.inventoryx.service.WarehouseStockService;

@RestController
@RequestMapping("/api/warehouse-stock")
public class WarehouseStockController {

    private final WarehouseStockService warehouseStockService;

    public WarehouseStockController(
            WarehouseStockService warehouseStockService) {
        this.warehouseStockService = warehouseStockService;
    }

    // ADD STOCK
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseStockResponseDTO> addStock(
            @Valid @RequestBody WarehouseStockRequestDTO request) {

        WarehouseStockResponseDTO response =
                warehouseStockService.addStock(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL STOCK
    @GetMapping
    public ResponseEntity<List<WarehouseStockResponseDTO>> getAllStock() {

        return ResponseEntity.ok(
                warehouseStockService.getAllStock()
        );
    }

    // GET STOCK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseStockResponseDTO> getStockById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                warehouseStockService.getStockById(id)
        );
    }

    // GET STOCK BY WAREHOUSE + PRODUCT
    @GetMapping("/warehouse/{warehouseId}/product/{productId}")
    public ResponseEntity<WarehouseStockResponseDTO> getStock(
            @PathVariable Long warehouseId,
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                warehouseStockService.getStock(
                        warehouseId,
                        productId
                )
        );
    }

    // REMOVE STOCK
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseStockResponseDTO> removeStock(
            @RequestParam Long warehouseId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        WarehouseStockResponseDTO response =
                warehouseStockService.removeStock(
                        warehouseId,
                        productId,
                        quantity
                );

        return ResponseEntity.ok(response);
    }
}