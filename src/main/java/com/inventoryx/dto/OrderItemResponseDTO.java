package com.inventoryx.dto;

public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private String productSku;

    private Integer quantity;
    private Double price;
    private Double subtotal;

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

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "OrderItemResponseDTO [productId=" + productId
                + ", productName=" + productName
                + ", productSku=" + productSku
                + ", quantity=" + quantity
                + ", price=" + price
                + ", subtotal=" + subtotal + "]";
    }
}