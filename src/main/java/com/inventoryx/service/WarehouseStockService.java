package com.inventoryx.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventoryx.dto.WarehouseStockRequestDTO;
import com.inventoryx.dto.WarehouseStockResponseDTO;
import com.inventoryx.entity.Product;
import com.inventoryx.entity.Warehouse;
import com.inventoryx.entity.WarehouseStock;
import com.inventoryx.exception.ProductNotFoundException;
import com.inventoryx.exception.WarehouseNotFoundException;
import com.inventoryx.exception.WarehouseStockNotFoundException;
import com.inventoryx.repository.ProductRepository;
import com.inventoryx.repository.WarehouseRepository;
import com.inventoryx.repository.WarehouseStockRepository;

@Service
public class WarehouseStockService {

    private final WarehouseStockRepository warehouseStockRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public WarehouseStockService(
            WarehouseStockRepository warehouseStockRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository) {

        this.warehouseStockRepository = warehouseStockRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    // ADD STOCK
    @Transactional
    public WarehouseStockResponseDTO addStock(
            WarehouseStockRequestDTO request) {

        Warehouse warehouse = warehouseRepository
                .findById(request.getWarehouseId())
                .orElseThrow(() ->
                        new WarehouseNotFoundException(
                                "Warehouse not found with id: "
                                        + request.getWarehouseId()));

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + request.getProductId()));

        WarehouseStock warehouseStock =
                warehouseStockRepository
                        .findByWarehouseIdAndProductId(
                                request.getWarehouseId(),
                                request.getProductId())
                        .orElse(null);

        if (warehouseStock == null) {

            warehouseStock = new WarehouseStock();

            warehouseStock.setWarehouse(warehouse);
            warehouseStock.setProduct(product);
            warehouseStock.setQuantity(request.getQuantity());

        } else {

            warehouseStock.setQuantity(
                    warehouseStock.getQuantity()
                            + request.getQuantity());
        }

        WarehouseStock savedStock =
                warehouseStockRepository.save(warehouseStock);

        return mapToResponseDTO(savedStock);
    }

    // GET ALL STOCK
    public List<WarehouseStockResponseDTO> getAllStock() {

        return warehouseStockRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // GET STOCK BY ID
    public WarehouseStockResponseDTO getStockById(Long id) {

        WarehouseStock warehouseStock =
                warehouseStockRepository.findById(id)
                        .orElseThrow(() ->
                                new WarehouseStockNotFoundException(
                                        "Warehouse stock not found with id: "
                                                + id));

        return mapToResponseDTO(warehouseStock);
    }

    // GET STOCK FOR SPECIFIC WAREHOUSE + PRODUCT
    public WarehouseStockResponseDTO getStock(
            Long warehouseId,
            Long productId) {

        WarehouseStock warehouseStock =
                warehouseStockRepository
                        .findByWarehouseIdAndProductId(
                                warehouseId,
                                productId)
                        .orElseThrow(() ->
                                new WarehouseStockNotFoundException(
                                        "Stock not found for warehouse "
                                                + warehouseId
                                                + " and product "
                                                + productId));

        return mapToResponseDTO(warehouseStock);
    }

    // REMOVE STOCK
    @Transactional
    public WarehouseStockResponseDTO removeStock(
            Long warehouseId,
            Long productId,
            Integer quantity) {

        WarehouseStock warehouseStock =
                warehouseStockRepository
                        .findByWarehouseIdAndProductId(
                                warehouseId,
                                productId)
                        .orElseThrow(() ->
                                new WarehouseStockNotFoundException(
                                        "Stock not found for warehouse "
                                                + warehouseId
                                                + " and product "
                                                + productId));

        if (warehouseStock.getQuantity() < quantity) {

            throw new IllegalArgumentException(
                    "Insufficient warehouse stock. Available: "
                            + warehouseStock.getQuantity()
                            + ", requested: "
                            + quantity);
        }

        warehouseStock.setQuantity(
                warehouseStock.getQuantity() - quantity);

        WarehouseStock updatedStock =
                warehouseStockRepository.save(warehouseStock);

        return mapToResponseDTO(updatedStock);
    }

    // ENTITY → RESPONSE DTO
    private WarehouseStockResponseDTO mapToResponseDTO(
            WarehouseStock warehouseStock) {

        WarehouseStockResponseDTO response =
                new WarehouseStockResponseDTO();

        response.setId(warehouseStock.getId());

        response.setWarehouseId(
                warehouseStock.getWarehouse().getId());

        response.setWarehouseName(
                warehouseStock.getWarehouse().getName());

        response.setProductId(
                warehouseStock.getProduct().getId());

        response.setProductName(
                warehouseStock.getProduct().getName());

        response.setProductSku(
                warehouseStock.getProduct().getSku());

        response.setQuantity(
                warehouseStock.getQuantity());

        response.setCreatedAt(
                warehouseStock.getCreatedAt());

        response.setUpdatedAt(
                warehouseStock.getUpdatedAt());

        return response;
    }
}