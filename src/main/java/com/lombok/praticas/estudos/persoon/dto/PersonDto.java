package com.lombok.praticas.estudos.persoon.dto;

import com.lombok.praticas.estudos.address.dto.AddressDto;
import com.lombok.praticas.estudos.persoon.Person;

import static com.lombok.praticas.estudos.persoon.utils.Utils.convertEntityToDtoAddress;

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
