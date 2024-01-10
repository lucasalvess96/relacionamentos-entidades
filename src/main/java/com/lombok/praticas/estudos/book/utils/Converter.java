package com.lombok.praticas.estudos.book.utils;

import com.lombok.praticas.estudos.book.Dto.BookDto;
import com.lombok.praticas.estudos.bookId.BookId;

public class Converter {

    public static BookId convertBookIdfromDto(BookDto bookDto) {
        if (bookDto != null)
            return new BookId(bookDto.title(), bookDto.language());
        return null;
    }
}
