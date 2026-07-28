package com.campusfood.controller;

import com.campusfood.dto.FoodResponse;
import com.campusfood.service.FoodService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/admin/foods")
public class AdminFoodController {
    private final FoodService foodService;

    public AdminFoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PutMapping("/{id}/stock")
    public FoodResponse updateStock(@PathVariable Long id, @RequestParam @Min(0) int stock) {
        return foodService.updateStock(id, stock);
    }
}

