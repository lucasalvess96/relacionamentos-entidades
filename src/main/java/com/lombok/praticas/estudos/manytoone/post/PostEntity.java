package com.lombok.praticas.estudos.manytoone.post;

import com.lombok.praticas.estudos.manytoone.author.AuthorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity authorEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostEntity that)) return false;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getTitle(), that.getTitle())
                && Objects.equals(getAuthorEntity(), that.getAuthorEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAuthorEntity());
    }
}
