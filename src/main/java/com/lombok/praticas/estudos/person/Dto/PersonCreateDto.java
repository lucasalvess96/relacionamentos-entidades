package com.lombok.praticas.estudos.person.Dto;

import com.lombok.praticas.estudos.person.PersonEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PersonCreateDto(

        Long id,

        @NotBlank(message = "Nome nao deve está vazio") 
        @Size(min = 3, max = 32) 
        @Pattern(regexp = "^" + "([A-Za-z]+[A-Za-z ])*$", message = "nome deve conter apenas letras") 
        String name,

        @NotBlank(message = "idade deve ser preenchida") 
        @Size(min = 2, max = 2)
        String age,

        @NotBlank(message = "CPF deve ser preenchido")
        String cpf
) {
    public PersonCreateDto(PersonEntity personEntity) {
        this(personEntity.getId(), personEntity.getName(), personEntity.getAge(), personEntity.getCpf());
    }
}
