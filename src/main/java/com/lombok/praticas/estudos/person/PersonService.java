package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.comum.ValidateCpfUser;
import com.lombok.praticas.estudos.person.comum.ValidateUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    
    public Page<PersonCreateDto> personListPagination(Pageable pageable) {
        Page<PersonEntity> personEntityPage = personRepository.findAll(pageable);
        return personEntityPage.map(PersonCreateDto::new);
    }

    public List<PersonCreateDto> personList() {
        List<PersonEntity> personEntities = personRepository.findAll();
        return personEntities.stream()
                .map(PersonCreateDto::new)
                .collect(Collectors.toList());
    }
}
