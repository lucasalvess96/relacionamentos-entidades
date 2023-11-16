package com.lombok.praticas.estudos.Post.Dto;

import com.lombok.praticas.estudos.Author.AuthorDto;
import com.lombok.praticas.estudos.Author.AuthorEntity;
import com.lombok.praticas.estudos.Post.PostEntity;
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
        return new AuthorDto(authorEntity.getId(), authorEntity.getName());
    }
}
