package com.lombok.praticas.estudos.book.utils;

import com.lombok.praticas.estudos.book.dto.BookDto;
import com.lombok.praticas.estudos.bookId.BookId;

public final class Converter {

    private Converter() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static BookId convertBookIdfromDto(BookDto bookDto) {
        if (bookDto != null)
            return new BookId(bookDto.title(), bookDto.language());
        return null;
    }
}
