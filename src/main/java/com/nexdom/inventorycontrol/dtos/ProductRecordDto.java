package com.nexdom.inventorycontrol.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexdom.inventorycontrol.enums.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRecordDto(@JsonView({ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @NotBlank(message = "Código é obrigatorio", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @Size(max = 20, message = "O código deve ter no máximo 20 caracteres", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @Pattern(regexp = "^[A-Za-z0-9_-]{1,20}$",
                                       message = "O código aceita apenas letras maiúsculas, dígitos, _ ou -",
                                       groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               String code,

                               @JsonView(ProductView.ProductPost.class)
                               @NotNull(message = "Tipo de produto é obrigatório",
                                       groups = ProductView.ProductPost.class)
                               ProductType productType,

                               @JsonView(ProductView.ProductPost.class)
                               @NotNull(message = "Fornecedor é obrigatorio",
                                       groups = ProductView.ProductPost.class)
                               UUID supplier,

                               @JsonView({ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @NotNull(message = "Preço no fornecedor é obrigatorio",
                                       groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @Positive(message = "Preço no fornecedor deve ser maior que zero", groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @Digits(integer = 10, fraction = 2,
                                      message = "Preço no fornecedor suporta no maximo 10 digitos e 2 decimais",
                                      groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               BigDecimal supplierPrice,

                               @JsonView({ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @NotNull(message = "Quantidade no estoque é obrigatório",
                                       groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @PositiveOrZero(message = "Quantidade no estoque não pode ser negativo",
                                       groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               @Max(value = 1_000_000, message = "Quantidade no estoque acima do limite permitido",
                                       groups = {ProductView.ProductPost.class, ProductView.ProductPut.class})
                               Integer stockQuantity){
    public interface ProductView {
        interface ProductPost {}
        interface ProductPut {}
    }
}
