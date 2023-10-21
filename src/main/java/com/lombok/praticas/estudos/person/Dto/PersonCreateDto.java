package com.lombok.praticas.estudos.person.Dto;

import com.lombok.praticas.estudos.person.PersonEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PersonCreateDto(

        Long id,

        @NotBlank(message = "O campo NOME não deve está vazio")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O campo NOME deve conter apenas letras") 
        String name,

        @NotBlank(message = "O campo IDADE não deve está vazio")        
        @Size(min = 2, max = 2, message = "O campo IDADE deve possuir apenas 2 valores")
        @Pattern(regexp = "\\d+", message = "O campo IDADE deve conter apenas números")
        String age,

        @NotBlank(message = "O campo CPF não deve está vazio")
        @Size(min = 14, max = 14, message = "O campo CPF deve possuir apenas 11 dígitos")
        @Pattern(regexp = "\\d+", message = "O campo CPF deve conter apenas números")
        String cpf
) {
    public PersonCreateDto(PersonEntity personEntity) {
        this(
                personEntity.getId(), 
                personEntity.getName(), 
                personEntity.getAge(), 
                personEntity.getCpf());
    }
}
