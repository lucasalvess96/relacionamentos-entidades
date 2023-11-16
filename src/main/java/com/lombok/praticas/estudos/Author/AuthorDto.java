package com.lombok.praticas.estudos.Author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthorDto(

        Long id,

        @NotBlank(message = "O campo NOME não deve está vazio")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O campo NOME deve conter apenas letras")
        String name

) {
    public AuthorDto(AuthorEntity author) {
        this(
                author.getId(),
                author.getName()
        );
    }
}
