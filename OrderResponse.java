package com.campusfood.dto;

import com.campusfood.domain.OrderEntity;
import com.campusfood.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(Long id, String orderNo, String studentName, String pickupSlot,
                            Long foodId, String foodName, Integer quantity, BigDecimal totalAmount,
                            OrderStatus status, LocalDateTime createdAt) {
    public static OrderResponse from(OrderEntity order) {
        return new OrderResponse(order.getId(), order.getOrderNo(), order.getStudentName(), order.getPickupSlot(),
                order.getFoodId(), order.getFoodName(), order.getQuantity(), order.getTotalAmount(),
                order.getStatus(), order.getCreatedAt());
    }
}

