package com.lombok.praticas.estudos.embeddedid.book.dto;

import com.lombok.praticas.estudos.embeddedid.book.BookEntity;

public record BookDto(String name, String genero, String title, String language) {

    public BookDto(BookEntity bookEntity) {
        this(
                bookEntity.getName(),
                bookEntity.getGenero(),
                bookEntity.getBookId().getTitle(),
                bookEntity.getBookId().getLanguage()
        );
    }
}
