package com.campusfood.config;

import com.campusfood.domain.FoodItem;
import com.campusfood.repository.FoodItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class SeedDataConfig {
    @Bean
    CommandLineRunner seedFood(FoodItemRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    new FoodItem("照烧鸡腿饭", "主食", "热乎的鸡腿配时蔬，午餐时段供应", new BigDecimal("18.00"), 24, "🍱"),
                    new FoodItem("番茄鸡蛋面", "面食", "清爽汤面，适合赶课时快速取餐", new BigDecimal("12.00"), 18, "🍜"),
                    new FoodItem("冰柠檬茶", "饮品", "少糖可选，和主食一起下单更方便", new BigDecimal("6.00"), 36, "🍋"),
                    new FoodItem("紫菜蛋花汤", "汤品", "午餐搭配汤品，售罄后自动提示", new BigDecimal("5.00"), 12, "🥣")
            ));
        };
    }
}

