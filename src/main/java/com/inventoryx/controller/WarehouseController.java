package com.inventoryx.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventoryx.dto.WarehouseRequestDTO;
import com.inventoryx.dto.WarehouseResponseDTO;
import com.inventoryx.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(
            @Valid @RequestBody WarehouseRequestDTO request) {

        WarehouseResponseDTO response =
                warehouseService.createWarehouse(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<WarehouseResponseDTO>> getAllWarehouses() {

        return ResponseEntity.ok(
                warehouseService.getAllWarehouses()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                warehouseService.getWarehouseById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequestDTO request) {

        return ResponseEntity.ok(
                warehouseService.updateWarehouse(id, request)
        );
    }

    // DEACTIVATE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateWarehouse(
            @PathVariable Long id) {

        warehouseService.deactivateWarehouse(id);

        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseResponseDTO> activateWarehouse(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                warehouseService.activateWarehouse(id)
        );
    }
}