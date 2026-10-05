package com.inventoryx.dto;

public class InventoryReportDTO {

    private long totalProducts;
    private long totalStock;
    private long lowStockProducts;
    private long totalWarehouses;

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(long totalStock) {
        this.totalStock = totalStock;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public long getTotalWarehouses() {
        return totalWarehouses;
    }

    public void setTotalWarehouses(long totalWarehouses) {
        this.totalWarehouses = totalWarehouses;
    }

    @Override
    public String toString() {
        return "InventoryReportDTO [totalProducts=" + totalProducts
                + ", totalStock=" + totalStock
                + ", lowStockProducts=" + lowStockProducts
                + ", totalWarehouses=" + totalWarehouses + "]";
    }
}