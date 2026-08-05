package com.inventoryx.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductRequestDTO {

	@NotBlank(message = "Product name is required")
	@Size(min = 2 , max = 100,
	      message = "Product name must be between 2 and 100 characters ")
	private String name;
	
	@Positive(message = "Price must be greater than zero")
	private Double price;
	
	@PositiveOrZero(message = "Quantity cannot be negative")
	private Integer quantity;
	
	@NotBlank(message = "SKU is required")
	private String sku;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	        
	
}
