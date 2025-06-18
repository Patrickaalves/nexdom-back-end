package com.nexdom.inventorycontrol.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupplierRecordDto(@JsonView({SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @NotBlank(message = "Código é obrigatório",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @Size(max = 20, message = "O código deve ter no máximo 20 caracteres",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @Pattern(regexp = "^[A-Za-z0-9_-]{1,20}$",
                                        message = "O código aceita apenas letras, dígitos, _ ou -",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                String code,

                                @JsonView({SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @NotBlank(message = "Nome é obrigatório",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                String name,

                                @JsonView({SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @NotBlank(message = "CNPJ é obrigatório",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @Pattern(regexp = "\\d{14}",
                                        message = "CNPJ deve conter exatamente 14 dígitos numéricos",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                String cnpj,

                                @JsonView({SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                @Pattern(regexp = "\\d{10,11}",
                                        message = "Telefone deve conter entre 11,10 dígitos numéricos",
                                        groups = {SupplierView.SupplierPost.class, SupplierView.SupplierPut.class})
                                String phone) {
    public interface SupplierView {
        interface SupplierPost {}
        interface SupplierPut  {}
    }
}


