package com.lombok.praticas.estudos.bookId;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookId implements Serializable {

    private String title;

    private String language;
}
