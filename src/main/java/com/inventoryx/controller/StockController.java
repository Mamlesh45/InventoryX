package com.inventoryx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import com.inventoryx.dto.StockRequestDTO;
import com.inventoryx.entity.StockMovement;
import com.inventoryx.payload.ApiResponse;
import com.inventoryx.service.StockService;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    
    @PostMapping("/in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StockMovement>> stockIn(
            @Valid @RequestBody StockRequestDTO dto) {

        StockMovement movement = stockService.stockIn(dto);

        ApiResponse<StockMovement> response =
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Stock added successfully",
                        movement
                );

        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/out")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StockMovement>> stockOut(
            @Valid @RequestBody StockRequestDTO dto) {

        StockMovement movement = stockService.stockOut(dto);

        ApiResponse<StockMovement> response =
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Stock removed successfully",
                        movement
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StockMovement>>> getStockHistory(
            @PathVariable Long productId) {

        List<StockMovement> movements =
                stockService.getStockHistory(productId);

        ApiResponse<List<StockMovement>> response =
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Stock history fetched successfully",
                        movements
                );

        return ResponseEntity.ok(response);
    }
}