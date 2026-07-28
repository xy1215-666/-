package com.campusfood.dto;

import com.campusfood.domain.FoodItem;

import java.math.BigDecimal;

public record FoodResponse(Long id, String name, String category, String description,
                           String imageUrl, BigDecimal price, Integer stock, boolean available) {
    public static FoodResponse from(FoodItem food) {
        return new FoodResponse(food.getId(), food.getName(), food.getCategory(), food.getDescription(),
                food.getImageUrl(), food.getPrice(), food.getStock(), food.isAvailable());
    }
}

