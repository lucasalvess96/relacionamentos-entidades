package com.lombok.praticas.estudos.post.dto;

import com.lombok.praticas.estudos.author.AuthorDto;
import com.lombok.praticas.estudos.author.AuthorEntity;
import com.lombok.praticas.estudos.post.PostEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PostDto(
        Long id,
        @NotBlank(message = "O campo NOME não deve está vazio")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O campo NOME deve conter apenas letras")
        String title,
        @Valid
        AuthorDto authorDto
) {

    public PostDto(PostEntity postEntity) {
        this(
                postEntity.getId(),
                postEntity.getTitle(),
                convertToAuthorDto(postEntity.getAuthorEntity())
        );
    }

    private static AuthorDto convertToAuthorDto(AuthorEntity authorEntity) {
        return new AuthorDto(authorEntity.getName());
    }
}
