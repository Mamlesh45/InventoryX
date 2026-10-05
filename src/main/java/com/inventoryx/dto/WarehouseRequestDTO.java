package com.inventoryx.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WarehouseRequestDTO {

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Warehouse name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 50, message = "Warehouse code cannot exceed 50 characters")
    private String code;

    @NotBlank(message = "Warehouse location is required")
    @Size(max = 200, message = "Warehouse location cannot exceed 200 characters")
    private String location;

    @NotNull(message = "Warehouse capacity is required")
    @Min(value = 1, message = "Warehouse capacity must be at least 1")
    private Integer capacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "WarehouseRequestDTO [name=" + name
                + ", code=" + code
                + ", location=" + location
                + ", capacity=" + capacity + "]";
    }
}