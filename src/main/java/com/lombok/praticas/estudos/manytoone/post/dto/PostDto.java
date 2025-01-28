package com.lombok.praticas.estudos.manytoone.post.dto;

import com.lombok.praticas.estudos.manytoone.author.AuthorDto;
import com.lombok.praticas.estudos.manytoone.author.AuthorEntity;
import com.lombok.praticas.estudos.manytoone.post.PostEntity;
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
