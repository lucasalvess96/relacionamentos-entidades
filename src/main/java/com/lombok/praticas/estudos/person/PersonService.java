package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.comum.ValidateCpfUser;
import com.lombok.praticas.estudos.person.comum.ValidateUser;
import org.springframework.stereotype.Service;

@Service
public record PersonService(PersonRepository personRepository) {
    
    public PersonCreateDto personCreate(PersonCreateDto personCreateDto) {
        ValidateUser.validateUser(personCreateDto, personRepository);
        ValidateCpfUser.validateCpfUser(personCreateDto, personRepository);
        
        PersonEntity person = new PersonEntity();
        person.setId(personCreateDto.id());
        person.setName(personCreateDto.name());
        person.setAge(personCreateDto.age());
        person.setCpf(personCreateDto.cpf());
        
        return new PersonCreateDto(personRepository.save(person));
    }
}
