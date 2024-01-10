package com.lombok.praticas.estudos.book.Dto;

import com.lombok.praticas.estudos.book.BookEntity;

public record BookDto(Long id, String name, String genero, String title, String language) {

    public BookDto(BookEntity bookEntity) {
        this(
                bookEntity.getId(),
                bookEntity.getName(),
                bookEntity.getGenero(),
                bookEntity.getBookId()
                        .getTitle(),
                bookEntity.getBookId()
                        .getLanguage()
        );
    }
}
