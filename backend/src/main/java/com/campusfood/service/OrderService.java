package com.campusfood.service;

import com.campusfood.domain.FoodItem;
import com.campusfood.domain.OrderEntity;
import com.campusfood.domain.OrderStatus;
import com.campusfood.dto.CreateOrderRequest;
import com.campusfood.dto.OrderResponse;
import com.campusfood.exception.BusinessException;
import com.campusfood.repository.FoodItemRepository;
import com.campusfood.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final FoodItemRepository foodRepository;
    private final OrderRepository orderRepository;
    private final FoodService foodService;

    public OrderService(FoodItemRepository foodRepository, OrderRepository orderRepository, FoodService foodService) {
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
        this.foodService = foodService;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        FoodItem food = foodService.getForOrder(request.foodId());
        if (food.getStock() < request.quantity()) {
            throw new BusinessException("库存不足，请选择其他餐品或时间");
        }

        food.setStock(food.getStock() - request.quantity());
        if (food.getStock() == 0) {
            food.setAvailable(false);
        }
        foodRepository.save(food);

        OrderEntity order = new OrderEntity();
        order.setOrderNo("CF" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        order.setStudentName(request.studentName());
        order.setPickupSlot(request.pickupSlot());
        order.setFoodId(food.getId());
        order.setFoodName(food.getName());
        order.setQuantity(request.quantity());
        order.setTotalAmount(food.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
        order.setStatus(OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponse find(Long id) {
        return orderRepository.findById(id).map(OrderResponse::from)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public List<OrderResponse> recent() {
        return orderRepository.findTop20ByOrderByCreatedAtDesc().stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse cancel(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new BusinessException("当前订单状态不允许取消");
        }
        FoodItem food = foodRepository.findById(order.getFoodId())
                .orElseThrow(() -> new BusinessException("关联餐品不存在"));
        food.setStock(food.getStock() + order.getQuantity());
        food.setAvailable(true);
        foodRepository.save(food);
        order.setStatus(OrderStatus.CANCELLED);
        return OrderResponse.from(orderRepository.save(order));
    }
}

