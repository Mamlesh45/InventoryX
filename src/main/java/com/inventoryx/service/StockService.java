package com.inventoryx.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventoryx.dto.StockRequestDTO;
import com.inventoryx.entity.Product;
import com.inventoryx.entity.StockMovement;
import com.inventoryx.entity.StockMovementType;
import com.inventoryx.exception.InsufficientStockException;
import com.inventoryx.exception.ProductNotFoundException;
import com.inventoryx.repository.ProductRepository;
import com.inventoryx.repository.StockMovementRepository;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockService(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository) {

        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public StockMovement stockIn(StockRequestDTO dto) {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + dto.getProductId()
                        )
                );

        product.setQuantity(
                product.getQuantity() + dto.getQuantity()
        );

        productRepository.save(product);

        StockMovement movement = new StockMovement();

        movement.setProduct(product);
        movement.setType(StockMovementType.IN);
        movement.setQuantity(dto.getQuantity());
        movement.setReason(dto.getReason());
        movement.setCreatedAt(LocalDateTime.now());

        return stockMovementRepository.save(movement);
    }

    public StockMovement stockOut(StockRequestDTO dto) {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + dto.getProductId()
                        )
                );

        if (product.getQuantity() < dto.getQuantity()) {

            throw new InsufficientStockException(
                    "Insufficient stock. Available quantity: "
                    + product.getQuantity()
            );
        }

        product.setQuantity(
                product.getQuantity() - dto.getQuantity()
        );

        productRepository.save(product);

        StockMovement movement = new StockMovement();

        movement.setProduct(product);
        movement.setType(StockMovementType.OUT);
        movement.setQuantity(dto.getQuantity());
        movement.setReason(dto.getReason());
        movement.setCreatedAt(LocalDateTime.now());

        return stockMovementRepository.save(movement);
    }
    
    public List<StockMovement> getStockHistory(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        return stockMovementRepository.findByProductId(product.getId());
    }
    
}