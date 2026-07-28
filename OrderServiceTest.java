package com.campusfood.service;

import com.campusfood.domain.FoodItem;
import com.campusfood.domain.OrderEntity;
import com.campusfood.dto.CreateOrderRequest;
import com.campusfood.exception.BusinessException;
import com.campusfood.repository.FoodItemRepository;
import com.campusfood.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    FoodItemRepository foodRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    FoodService foodService;

    @InjectMocks
    OrderService orderService;

    @Test
    void createShouldRejectWhenStockIsNotEnough() {
        FoodItem food = new FoodItem("照烧鸡腿饭", "主食", "", new BigDecimal("18.00"), 1, "🍱");
        when(foodService.getForOrder(1L)).thenReturn(food);

        CreateOrderRequest request = new CreateOrderRequest("小林", "12:00-12:20", 1L, 2);

        assertThrows(BusinessException.class, () -> orderService.create(request));
    }

    @Test
    void createShouldReduceStockAndSaveOrder() {
        FoodItem food = new FoodItem("照烧鸡腿饭", "主食", "", new BigDecimal("18.00"), 5, "🍱");
        food.setId(1L);
        when(foodService.getForOrder(1L)).thenReturn(food);
        when(foodRepository.save(any(FoodItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderEntity saved = new OrderEntity();
        saved.setId(100L);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(saved);

        OrderEntity result = new OrderEntity();
        result.setId(100L);
        result.setStatus(com.campusfood.domain.OrderStatus.PREPARING);

        orderService.create(new CreateOrderRequest("小林", "12:00-12:20", 1L, 2));

        assertEquals(3, food.getStock());
    }
}

