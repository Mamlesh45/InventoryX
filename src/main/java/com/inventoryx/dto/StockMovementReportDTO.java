package com.inventoryx.dto;

public class StockMovementReportDTO {

    private long totalMovements;
    private long totalStockIn;
    private long totalStockOut;

    public long getTotalMovements() {
        return totalMovements;
    }

    public void setTotalMovements(long totalMovements) {
        this.totalMovements = totalMovements;
    }

    public long getTotalStockIn() {
        return totalStockIn;
    }

    public void setTotalStockIn(long totalStockIn) {
        this.totalStockIn = totalStockIn;
    }

    public long getTotalStockOut() {
        return totalStockOut;
    }

    public void setTotalStockOut(long totalStockOut) {
        this.totalStockOut = totalStockOut;
    }

    @Override
    public String toString() {
        return "StockMovementReportDTO [totalMovements="
                + totalMovements
                + ", totalStockIn=" + totalStockIn
                + ", totalStockOut=" + totalStockOut + "]";
    }
}