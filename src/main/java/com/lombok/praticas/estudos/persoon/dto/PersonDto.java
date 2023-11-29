package com.lombok.praticas.estudos.persoon.dto;

import com.lombok.praticas.estudos.address.dto.AddressDto;
import com.lombok.praticas.estudos.persoon.Person;

import static com.lombok.praticas.estudos.persoon.utils.Utils.convertEntityToDtoAddress;

public record PersonDto(String name, String cpf, AddressDto addressDto) {

    public PersonDto(Person person) {
        this(
                person.getName(), person.getCpf(), convertEntityToDtoAddress(person.getAddress())
        );
    }
}
