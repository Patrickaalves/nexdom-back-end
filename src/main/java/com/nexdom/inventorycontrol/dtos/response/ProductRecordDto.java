package com.nexdom.inventorycontrol.dtos.response;

import com.nexdom.inventorycontrol.enums.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank(message = "code is mandatory", groups = ProductView.ProductPost.class)
                         @Size(max = 20, message = "code must have at most 20 characters", groups = ProductView.ProductPost.class)
                         @Pattern(
                                 regexp = "^[A-Za-z0-9_-]{1,20}$",
                                 message = "code accepts only upper‑case letters, digits, _ or -"
                                 , groups = ProductView.ProductPost.class)
                         String code,

                         @NotNull(message = "productType is mandatory", groups = ProductView.ProductPost.class)
                         ProductType productType,

                         @NotNull(message = "supplierPrice is mandatory", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         @Positive(message = "supplierPrice must be greater than zero", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         @Digits(integer = 10, fraction = 2,
                                 message = "supplierPrice supports max 10 integer digits and 2 decimals",
                                 groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         BigDecimal supplierPrice,

                         @NotNull(message = "stockQuantity is mandatory", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         @PositiveOrZero(message = "stockQuantity cannot be negative", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         @Max(value = 1_000_000, message = "stockQuantity above the allowed limit", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                         Integer stockQuantity)
{
    public interface ProductView {
        interface ProductPost {}
        interface ProductPut {}
    }
}
