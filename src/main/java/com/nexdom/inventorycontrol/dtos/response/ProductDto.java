package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductDto(@NotBlank(message = "code is mandatory")
                         @Size(max = 20, message = "code must have at most 20 characters")
                         @Pattern(
                                 regexp = "^[A-Za-z0-9_-]{1,20}$",
                                 message = "code accepts only upper‑case letters, digits, _ or -"
                         )
                         String code,

                         @NotNull(message = "productType is mandatory")
                         ProductType productType,

                         @NotNull(message = "supplierPrice is mandatory")
                         @Positive(message = "supplierPrice must be greater than zero")
                         @Digits(integer = 10, fraction = 2,
                                 message = "supplierPrice supports max 10 integer digits and 2 decimals")
                         BigDecimal supplierPrice,

                         @NotNull(message = "stockQuantity is mandatory")
                         @PositiveOrZero(message = "stockQuantity cannot be negative")
                         @Max(value = 1_000_000, message = "stockQuantity above the allowed limit")
                         Integer stockQuantity)
{}
