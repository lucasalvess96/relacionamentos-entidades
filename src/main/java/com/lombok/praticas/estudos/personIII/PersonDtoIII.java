package com.lombok.praticas.estudos.personIII;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PersonDtoIII(
        @Schema(name = "identificação usuário", example = "123")
        Long id,

        @NotBlank(message = "O campo NOME não deve está vazio")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O campo NOME deve conter apenas letras")
        @Schema(name = "Nome usuário", example = "John Doe")
        String name,

        @NotBlank(message = "O campo IDADE não deve está vazio")
        @Size(min = 2, max = 2, message = "O campo IDADE deve possuir apenas 2 dígitos")
        @Pattern(regexp = "\\d+", message = "O campo IDADE deve conter apenas números")
        @Schema(name = "idade usuário", example = "21")
        String age,

        @NotBlank(message = "O campo CPF não deve está vazio")
        @Size(min = 11, max = 11, message = "O campo CPF deve possuir apenas 11 dígitos")
        @Pattern(regexp = "\\d+", message = "O campo CPF deve conter apenas números")
        @Schema(name = "CPF usuário", example = "011.001.0101-10")
        String cpf
) { }
