package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.PersonRepository;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;

public class ValidateCpfUser {

    private ValidateCpfUser() {
        throw new UnsupportedOperationException("Esta classe de utilitário não pode ser instanciada.");
    }

    public static void validateCpfUser(PersonCreateDto personCreateDto, PersonRepository personRepository) {
        if (personRepository.existsByCpf(personCreateDto.cpf())) {
            throw new ErroRequest("Este CPF já existe na base de dados.");
        }
    }
}
