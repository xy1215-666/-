package com.campusfood.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotBlank String studentName,
        @NotBlank String pickupSlot,
        @NotNull Long foodId,
        @NotNull @Min(1) Integer quantity
) {
}

