package com.lombok.praticas.estudos.onetoone.persoon.dto;

import com.lombok.praticas.estudos.onetoone.address.dto.AddressDto;
import com.lombok.praticas.estudos.onetoone.persoon.Person;

import static com.lombok.praticas.estudos.onetoone.persoon.utils.Utils.convertEntityToDtoAddress;

public record PersonDto(Long id, String name, String cpf, AddressDto addressDto) {

    public PersonDto(Person person) {
        this(
                person.getId(),
                person.getName(),
                person.getCpf(),
                convertEntityToDtoAddress(person.getAddress())
        );
    }
}
