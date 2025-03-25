package com.lombok.praticas.estudos.person;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PERSON", schema = "CRUD")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String age;

    private String cpf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonEntity that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
                && Objects.equals(getAge(), that.getAge()) && Objects.equals(getCpf(), that.getCpf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge(), getCpf());
    }
}
