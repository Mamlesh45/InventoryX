package com.inventoryx.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventoryx.dto.InventoryReportDTO;
import com.inventoryx.dto.LowStockReportDTO;
import com.inventoryx.dto.SalesReportDTO;
import com.inventoryx.dto.StockMovementReportDTO;
import com.inventoryx.entity.OrderStatus;
import com.inventoryx.entity.Product;
import com.inventoryx.entity.StockMovementType;
import com.inventoryx.entity.WarehouseStock;
import com.inventoryx.repository.OrderRepository;
import com.inventoryx.repository.ProductRepository;
import com.inventoryx.repository.StockMovementRepository;
import com.inventoryx.repository.WarehouseRepository;
import com.inventoryx.repository.WarehouseStockRepository;

@Service
public class ReportsService {

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final OrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;

    public ReportsService(
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            WarehouseStockRepository warehouseStockRepository,
            OrderRepository orderRepository,
            StockMovementRepository stockMovementRepository) {

        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.orderRepository = orderRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    // INVENTORY REPORT

    public InventoryReportDTO getInventoryReport() {

        InventoryReportDTO report =
                new InventoryReportDTO();

        long totalProducts =
                productRepository.count();

        long totalWarehouses =
                warehouseRepository.count();

        long totalStock =
                warehouseStockRepository.findAll()
                        .stream()
                        .mapToLong(stock ->
                                stock.getQuantity())
                        .sum();

        long lowStockProducts =
                warehouseStockRepository
                        .countByQuantityLessThanEqual(10);

        report.setTotalProducts(totalProducts);
        report.setTotalWarehouses(totalWarehouses);
        report.setTotalStock(totalStock);
        report.setLowStockProducts(lowStockProducts);

        return report;
    }

    // LOW STOCK REPORT

    public List<LowStockReportDTO> getLowStockReport() {

        List<WarehouseStock> lowStock =
                warehouseStockRepository
                        .findAll()
                        .stream()
                        .filter(stock ->
                                stock.getQuantity() <= 10)
                        .toList();

        List<LowStockReportDTO> response =
                new ArrayList<>();

        for (WarehouseStock stock : lowStock) {

            Product product =
                    stock.getProduct();

            LowStockReportDTO dto =
                    new LowStockReportDTO();

            dto.setProductId(
                    product.getId());

            dto.setProductName(
                    product.getName());

            dto.setSku(
                    product.getSku());

            dto.setQuantity(
                    stock.getQuantity());

            response.add(dto);
        }

        return response;
    }

    // SALES REPORT

    public SalesReportDTO getSalesReport() {

        SalesReportDTO report =
                new SalesReportDTO();

        long totalOrders =
                orderRepository.count();

        long completedOrders =
                orderRepository.countByStatus(
                        OrderStatus.DELIVERED);

        long cancelledOrders =
                orderRepository.countByStatus(
                        OrderStatus.CANCELLED);

        Double totalSales =
                orderRepository.calculateTotalSales(
                        OrderStatus.CANCELLED);

        if (totalSales == null) {
            totalSales = 0.0;
        }

        report.setTotalOrders(totalOrders);
        report.setCompletedOrders(completedOrders);
        report.setCancelledOrders(cancelledOrders);
        report.setTotalSales(totalSales);

        return report;
    }

    // STOCK MOVEMENT REPORT 

    public StockMovementReportDTO
    getStockMovementReport() {

        StockMovementReportDTO report =
                new StockMovementReportDTO();

        long totalMovements =
                stockMovementRepository.count();

        Long totalStockIn =
                stockMovementRepository
                        .calculateTotalQuantityByType(
                                StockMovementType.IN);

        Long totalStockOut =
                stockMovementRepository
                        .calculateTotalQuantityByType(
                                StockMovementType.OUT);

        if (totalStockIn == null) {
            totalStockIn = 0L;
        }

        if (totalStockOut == null) {
            totalStockOut = 0L;
        }

        report.setTotalMovements(totalMovements);
        report.setTotalStockIn(totalStockIn);
        report.setTotalStockOut(totalStockOut);

        return report;
    }
}