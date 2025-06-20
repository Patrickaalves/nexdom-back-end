package com.nexdom.inventorycontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRecordDto(@NotBlank(message = "Código é obrigatório")
                                @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
                                @Pattern(regexp = "^[A-Za-z0-9_-]{1,20}$",
                                        message = "O código aceita apenas letras, dígitos, _ ou -")
                                String code,

                                @NotBlank(message = "O nome é obrigatório")
                                @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
                                String name,

                                @NotBlank(message = "O telefone é obrigatório")
                                @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números e ter 10 ou 11 dígitos")
                                String phone ){
}
