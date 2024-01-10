package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.bookId.BookId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookEntity {

    @EmbeddedId
    private BookId bookId;

    private String name;

    private String genero;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntity book)) return false;
        return Objects.equals(getBookId(), book.getBookId())
                && Objects.equals(getName(), book.getName())
                && Objects.equals(getGenero(), book.getGenero());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId(), getName(), getGenero());
    }
}
