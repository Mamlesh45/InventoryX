package com.inventoryx.dto;

public class SalesReportDTO {

    private long totalOrders;
    private long completedOrders;
    private long cancelledOrders;
    private double totalSales;

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    @Override
    public String toString() {
        return "SalesReportDTO [totalOrders=" + totalOrders
                + ", completedOrders=" + completedOrders
                + ", cancelledOrders=" + cancelledOrders
                + ", totalSales=" + totalSales + "]";
    }
}