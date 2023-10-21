package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.PersonRepository;

public class ValidateCpfUser {
    public static void validateCpfUser(PersonCreateDto personCreateDto, PersonRepository personRepository) {
        if (personRepository.existsByCpf(personCreateDto.cpf())) {
            throw new ErroRequest("Este CPF já existe na base de dados.");
        }
    }
}
