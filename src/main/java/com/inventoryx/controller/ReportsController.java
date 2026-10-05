package com.inventoryx.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventoryx.dto.InventoryReportDTO;
import com.inventoryx.dto.LowStockReportDTO;
import com.inventoryx.dto.SalesReportDTO;
import com.inventoryx.dto.StockMovementReportDTO;
import com.inventoryx.service.ReportsService;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    // INVENTORY REPORT
    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryReportDTO> getInventoryReport() {

        return ResponseEntity.ok(
                reportsService.getInventoryReport()
        );
    }

    // LOW STOCK REPORT
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LowStockReportDTO>> getLowStockReport() {

        return ResponseEntity.ok(
                reportsService.getLowStockReport()
        );
    }

    // SALES REPORT
    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalesReportDTO> getSalesReport() {

        return ResponseEntity.ok(
                reportsService.getSalesReport()
        );
    }

    // STOCK MOVEMENT REPORT
    @GetMapping("/stock-movements")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockMovementReportDTO> getStockMovementReport() {

        return ResponseEntity.ok(
                reportsService.getStockMovementReport()
        );
    }
}