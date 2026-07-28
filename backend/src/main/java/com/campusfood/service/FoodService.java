package com.campusfood.service;

import com.campusfood.domain.FoodItem;
import com.campusfood.dto.FoodResponse;
import com.campusfood.exception.BusinessException;
import com.campusfood.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodService {
    private final FoodItemRepository foodRepository;

    public FoodService(FoodItemRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<FoodResponse> listAvailable() {
        return foodRepository.findByAvailableTrueOrderByIdAsc().stream().map(FoodResponse::from).toList();
    }

    public FoodItem getForOrder(Long id) {
        FoodItem food = foodRepository.findById(id)
                .orElseThrow(() -> new BusinessException("餐品不存在"));
        if (!food.isAvailable()) {
            throw new BusinessException("餐品暂时不可购买");
        }
        return food;
    }

    @Transactional
    public FoodResponse updateStock(Long id, int stock) {
        if (stock < 0) {
            throw new BusinessException("库存不能为负数");
        }
        FoodItem food = foodRepository.findById(id)
                .orElseThrow(() -> new BusinessException("餐品不存在"));
        food.setStock(stock);
        food.setAvailable(stock > 0);
        return FoodResponse.from(foodRepository.save(food));
    }
}

