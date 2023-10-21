package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.comum.ValidateCpfUser;
import com.lombok.praticas.estudos.person.comum.ValidateUser;
import org.springframework.stereotype.Service;

@Service
public record PersonService(PersonRepository personRepository) {
    
    public PersonCreateDto personCreate(PersonCreateDto personCreateDto) {
        ValidateUser.validateUser(personCreateDto, personRepository);
        ValidateCpfUser.validateCpfUser(personCreateDto, personRepository);
        String cleanedCpf = ValidateCpfUser.removeSpecialCharactersFromCpf(personCreateDto.cpf());
        PersonCreateDto updatedDto = new PersonCreateDto(personCreateDto.id(), personCreateDto.name(), personCreateDto.age(), cleanedCpf);

        PersonEntity person = new PersonEntity();
        person.setId(updatedDto.id());
        person.setName(updatedDto.name());
        person.setAge(updatedDto.age());
        person.setCpf(updatedDto.cpf());
        
        return new PersonCreateDto(personRepository.save(person));
    }
}
