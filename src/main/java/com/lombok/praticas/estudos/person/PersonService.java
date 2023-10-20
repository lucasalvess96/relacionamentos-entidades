package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import org.springframework.stereotype.Service;

@Service
public record PersonService(PersonRepository personRepository) {
    
    public PersonCreateDto personCreate(PersonCreateDto personCreateDto) {
        PersonEntity person = new PersonEntity();
        person.setId(personCreateDto.id());
        person.setName(personCreateDto.name());
        person.setAge(personCreateDto.age());
        person.setCpf(personCreateDto.cpf());
        
        return new PersonCreateDto(personRepository.save(person));
    }
}
