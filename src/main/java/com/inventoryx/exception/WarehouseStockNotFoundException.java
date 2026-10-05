package com.inventoryx.exception;

public class WarehouseStockNotFoundException extends RuntimeException {

    public WarehouseStockNotFoundException(String message) {
        super(message);
    }
}