package com.lombok.praticas.estudos.onetoone.address;

import com.lombok.praticas.estudos.onetoone.persoon.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String street;

    private Long number;

    private String city;

    @OneToOne(mappedBy = "address")
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEntity that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStreet(), that.getStreet())
                && Objects.equals(getNumber(), that.getNumber()) && Objects.equals(getCity(),
                                                                                   that.getCity()
        ) && Objects.equals(getPerson(), that.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStreet(), getNumber(), getCity(), getPerson());
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", city='" + city + '\'' +
                ", person=" + person +
                '}';
    }
}
