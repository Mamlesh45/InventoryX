package com.inventoryx.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventoryx.dto.OrderItemRequestDTO;
import com.inventoryx.dto.OrderItemResponseDTO;
import com.inventoryx.dto.OrderRequestDTO;
import com.inventoryx.dto.OrderResponseDTO;
import com.inventoryx.entity.Order;
import com.inventoryx.entity.OrderItem;
import com.inventoryx.entity.OrderStatus;
import com.inventoryx.entity.Product;
import com.inventoryx.entity.StockMovement;
import com.inventoryx.entity.StockMovementType;
import com.inventoryx.entity.User;
import com.inventoryx.entity.Warehouse;
import com.inventoryx.entity.WarehouseStock;
import com.inventoryx.exception.ProductNotFoundException;
import com.inventoryx.exception.WarehouseNotFoundException;
import com.inventoryx.exception.WarehouseStockNotFoundException;
import com.inventoryx.repository.OrderItemRepository;
import com.inventoryx.repository.OrderRepository;
import com.inventoryx.repository.ProductRepository;
import com.inventoryx.repository.StockMovementRepository;
import com.inventoryx.repository.UserRepository;
import com.inventoryx.repository.WarehouseRepository;
import com.inventoryx.repository.WarehouseStockRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockMovementRepository stockMovementRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            WarehouseStockRepository warehouseStockRepository,
            StockMovementRepository stockMovementRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

 
    @Transactional
    public OrderResponseDTO createOrder(
            Long userId,
            OrderRequestDTO request) {

        // 1. Get authenticated user
        User currentUser = getCurrentUser();

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMIN")
                        );

        User user;

        // ADMIN can create order for any user
        if (isAdmin && userId != null) {

            user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: " + userId));

        } else {

            // USER can only create order for themselves
            user = currentUser;
        }

        // 2. Find Warehouse
        Warehouse warehouse = warehouseRepository
                .findById(request.getWarehouseId())
                .orElseThrow(() ->
                        new WarehouseNotFoundException(
                                "Warehouse not found with id: "
                                        + request.getWarehouseId()));

        // 3. Make sure warehouse is active
        if (!Boolean.TRUE.equals(warehouse.getActive())) {

            throw new IllegalArgumentException(
                    "Cannot create order from inactive warehouse");
        }

        // 4. Create Order
        Order order = new Order();

        order.setUser(user);
        order.setWarehouse(warehouse);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0.0);

        Order savedOrder =
                orderRepository.save(order);

        double totalAmount = 0.0;

        List<OrderItem> orderItems =
                new ArrayList<>();

        // 5. Process every requested item
        for (OrderItemRequestDTO itemRequest :
                request.getItems()) {

            Product product =
                    productRepository
                            .findById(
                                    itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new ProductNotFoundException(
                                            "Product not found with id: "
                                                    + itemRequest
                                                            .getProductId()));

            // 6. Find warehouse-specific stock
            WarehouseStock warehouseStock =
                    warehouseStockRepository
                            .findByWarehouseIdAndProductId(
                                    warehouse.getId(),
                                    product.getId())
                            .orElseThrow(() ->
                                    new WarehouseStockNotFoundException(
                                            "Stock not found for warehouse "
                                                    + warehouse.getId()
                                                    + " and product "
                                                    + product.getId()));

            // 7. Check available stock
            if (warehouseStock.getQuantity()
                    < itemRequest.getQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + warehouseStock.getQuantity()
                                + ", requested: "
                                + itemRequest.getQuantity());
            }

            // 8. Product price
            Double price =
                    product.getPrice();

            // 9. Calculate subtotal
            Double subtotal =
                    price * itemRequest.getQuantity();

            // 10. Deduct warehouse stock
            warehouseStock.setQuantity(
                    warehouseStock.getQuantity()
                            - itemRequest.getQuantity());

            warehouseStockRepository
                    .save(warehouseStock);

            // 11. Create stock movement OUT
            StockMovement movement =
                    new StockMovement();

            movement.setProduct(product);
            movement.setType(
                    StockMovementType.OUT);
            movement.setQuantity(
                    itemRequest.getQuantity());
            movement.setReason(
                    "Order stock deduction");

            stockMovementRepository
                    .save(movement);

            // 12. Create OrderItem
            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(
                    itemRequest.getQuantity());
            orderItem.setPrice(price);
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);

            // 13. Add to total
            totalAmount += subtotal;
        }

        // 14. Save OrderItems
        List<OrderItem> savedOrderItems =
                orderItemRepository
                        .saveAll(orderItems);

        // 15. Set total
        savedOrder.setTotalAmount(
                totalAmount);

        // 16. Confirm order
        savedOrder.setStatus(
                OrderStatus.CONFIRMED);

        Order finalOrder =
                orderRepository.save(savedOrder);

        // 17. Build response
        return mapToResponseDTO(
                finalOrder,
                savedOrderItems);
    }

    public OrderResponseDTO getOrderById(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + id));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMIN")
                        );

        if (!isAdmin) {

            User currentUser =
                    getCurrentUser();

            if (!order.getUser()
                    .getId()
                    .equals(currentUser.getId())) {

                throw new SecurityException(
                        "You are not allowed to access this order");
            }
        }

        List<OrderItem> items =
                orderItemRepository
                        .findByOrder(order);

        return mapToResponseDTO(
                order,
                items);
    }

    public List<OrderResponseDTO> getOrdersByUser(
            Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMIN")
                        );

        User user;

        if (isAdmin) {

            user = userRepository
                    .findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: "
                                            + userId));

        } else {

            user = getCurrentUser();
        }

        List<Order> orders =
                orderRepository.findByUser(user);

        List<OrderResponseDTO> responses =
                new ArrayList<>();

        for (Order order : orders) {

            List<OrderItem> items =
                    orderItemRepository
                            .findByOrder(order);

            responses.add(
                    mapToResponseDTO(
                            order,
                            items));
        }

        return responses;
    }

 
    @Transactional
    public OrderResponseDTO updateOrderStatus(
            Long id,
            OrderStatus newStatus) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + id));

        order.setStatus(newStatus);

        Order updatedOrder =
                orderRepository.save(order);

        List<OrderItem> items =
                orderItemRepository
                        .findByOrder(updatedOrder);

        return mapToResponseDTO(
                updatedOrder,
                items);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + id));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMIN")
                        );

        if (!isAdmin) {

            User currentUser =
                    getCurrentUser();

            if (!order.getUser()
                    .getId()
                    .equals(currentUser.getId())) {

                throw new SecurityException(
                        "You are not allowed to cancel this order");
            }
        }

        if (order.getStatus()
                == OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Delivered order cannot be cancelled");
        }

        if (order.getStatus()
                == OrderStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Order is already cancelled");
        }

        List<OrderItem> items =
                orderItemRepository
                        .findByOrder(order);

        for (OrderItem item : items) {

            WarehouseStock warehouseStock =
                    warehouseStockRepository
                            .findByWarehouseIdAndProductId(
                                    order.getWarehouse().getId(),
                                    item.getProduct().getId())
                            .orElseThrow(() ->
                                    new WarehouseStockNotFoundException(
                                            "Warehouse stock not found"));

            warehouseStock.setQuantity(
                    warehouseStock.getQuantity()
                            + item.getQuantity());

            warehouseStockRepository
                    .save(warehouseStock);

            StockMovement movement =
                    new StockMovement();

            movement.setProduct(
                    item.getProduct());

            movement.setType(
                    StockMovementType.IN);

            movement.setQuantity(
                    item.getQuantity());

            movement.setReason(
                    "Order cancellation - stock restored");

            stockMovementRepository
                    .save(movement);
        }

        order.setStatus(
                OrderStatus.CANCELLED);

        Order cancelledOrder =
                orderRepository.save(order);

        return mapToResponseDTO(
                cancelledOrder,
                items);
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"));
    }

    private OrderResponseDTO mapToResponseDTO(
            Order order,
            List<OrderItem> items) {

        OrderResponseDTO response =
                new OrderResponseDTO();

        response.setId(
                order.getId());

        response.setUserId(
                order.getUser().getId());

        response.setWarehouseId(
                order.getWarehouse().getId());

        response.setWarehouseName(
                order.getWarehouse().getName());

        response.setTotalAmount(
                order.getTotalAmount());

        response.setStatus(
                order.getStatus().name());

        response.setCreatedAt(
                order.getCreatedAt());

        response.setUpdatedAt(
                order.getUpdatedAt());

        List<OrderItemResponseDTO> itemResponses =
                new ArrayList<>();

        for (OrderItem item : items) {

            OrderItemResponseDTO itemResponse =
                    new OrderItemResponseDTO();

            itemResponse.setProductId(
                    item.getProduct().getId());

            itemResponse.setProductName(
                    item.getProduct().getName());

            itemResponse.setProductSku(
                    item.getProduct().getSku());

            itemResponse.setQuantity(
                    item.getQuantity());

            itemResponse.setPrice(
                    item.getPrice());

            itemResponse.setSubtotal(
                    item.getSubtotal());

            itemResponses.add(
                    itemResponse);
        }

        response.setItems(
                itemResponses);

        return response;
    }
}