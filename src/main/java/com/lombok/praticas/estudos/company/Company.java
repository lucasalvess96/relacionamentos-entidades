package com.lombok.praticas.estudos.company;

import com.lombok.praticas.estudos.contactperson.ContactPersonEntity;
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
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    @Embedded
    private ContactPersonEntity contactPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company company)) return false;
        return Objects.equals(getId(), company.getId()) && Objects.equals(getName(), company.getName())
                && Objects.equals(getAddress(), company.getAddress())
                && Objects.equals(getPhone(), company.getPhone())
                && Objects.equals(getContactPerson(), company.getContactPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress(), getPhone(), getContactPerson());
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", contactPerson=" + contactPerson +
                '}';
    }
}
