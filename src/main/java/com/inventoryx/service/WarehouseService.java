package com.inventoryx.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inventoryx.dto.WarehouseRequestDTO;
import com.inventoryx.dto.WarehouseResponseDTO;
import com.inventoryx.entity.Warehouse;
import com.inventoryx.exception.WarehouseNotFoundException;
import com.inventoryx.repository.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // CREATE
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO request) {

        Warehouse warehouse = new Warehouse();

        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setLocation(request.getLocation());
        warehouse.setCapacity(request.getCapacity());

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        return mapToResponseDTO(savedWarehouse);
    }

    // GET ALL
    public List<WarehouseResponseDTO> getAllWarehouses() {

        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public WarehouseResponseDTO getWarehouseById(Long id) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id: " + id));

        return mapToResponseDTO(warehouse);
    }

    // UPDATE
    public WarehouseResponseDTO updateWarehouse(
            Long id,
            WarehouseRequestDTO request) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id: " + id));

        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setLocation(request.getLocation());
        warehouse.setCapacity(request.getCapacity());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        return mapToResponseDTO(updatedWarehouse);
    }

    // DELETE / DEACTIVATE
    public void deactivateWarehouse(Long id) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id: " + id));

        warehouse.setActive(false);

        warehouseRepository.save(warehouse);
    }
    
    public WarehouseResponseDTO activateWarehouse(Long id) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException(
                                "Warehouse not found with id: " + id
                        )
                );

        warehouse.setActive(true);

        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        return mapToResponseDTO(savedWarehouse);
    }

    // ENTITY → RESPONSE DTO
    private WarehouseResponseDTO mapToResponseDTO(Warehouse warehouse) {

        WarehouseResponseDTO response = new WarehouseResponseDTO();

        response.setId(warehouse.getId());
        response.setName(warehouse.getName());
        response.setCode(warehouse.getCode());
        response.setLocation(warehouse.getLocation());
        response.setCapacity(warehouse.getCapacity());
        response.setActive(warehouse.getActive());
        response.setCreatedAt(warehouse.getCreatedAt());
        response.setUpdatedAt(warehouse.getUpdatedAt());

        return response;
    }
}