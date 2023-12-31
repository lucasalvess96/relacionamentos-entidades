package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.PersonRepository;

public class ValidateUser {

    public static void validateUser(PersonCreateDto personCreateDto, PersonRepository personRepository) {
        if (personRepository.existsByName(personCreateDto.name())) {
            throw new ErroRequest("Este usuário já existe na base de dados.");
        }
    }
}
