package com.inventoryx.dto;

public class LowStockReportDTO {

    private Long productId;
    private String productName;
    private String sku;
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "LowStockReportDTO [productId=" + productId
                + ", productName=" + productName
                + ", sku=" + sku
                + ", quantity=" + quantity + "]";
    }
}
