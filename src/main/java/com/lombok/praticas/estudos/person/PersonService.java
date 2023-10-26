package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.Dto.PersonSearchDto;
import com.lombok.praticas.estudos.person.comum.ValidateCpfUser;
import com.lombok.praticas.estudos.person.comum.ValidateUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record PersonService(PersonRepository personRepository) {
    
    public PersonCreateDto personCreate(PersonCreateDto personCreateDto) {
        ValidateUser.validateUser(personCreateDto, personRepository);
        ValidateCpfUser.validateCpfUser(personCreateDto, personRepository);
        PersonEntity personEntity = new PersonEntity();
        return getPersonCreateDtoObject(personCreateDto, personEntity);
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

    public PersonCreateDto personUpdate(Long id, PersonCreateDto personCreateDto) {
        PersonEntity personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ErroRequest("ID não encontrado"));
        return getPersonCreateDtoObject(personCreateDto, personEntity);
    }

    public Optional<PersonCreateDto> detailPerson(Long id) {
        Optional<PersonEntity> personEntity = personRepository.findById(id);
        return personEntity.map(entity -> Optional.of(new PersonCreateDto(entity)))
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
    }

    public Page<PersonSearchDto> searchPersonPagination(String name, Pageable pageable) {
        Page<PersonEntity> personEntityPage = personRepository.findByNameContainingIgnoreCase(name, pageable);
        return personEntityPage.map(personEntity -> new PersonSearchDto(personEntity.getName()));
    }

    public List<PersonSearchDto> searchListPerson(String name) {
        List<PersonEntity> personEntities = personRepository.findByNameContainingIgnoreCase(name);
        return personEntities.stream()
                .map(personEntity -> new PersonSearchDto(personEntity.getName()))
                .collect(Collectors.toList());
    }

    public void deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        } else {
            throw new ErroRequest("Recurso não encontrado");
        }
    }

    public PersonCreateDto getPersonCreateDtoObject(PersonCreateDto personCreateDto, PersonEntity person) {
        if(person == null) {
            throw new NullPointerException("PersonEntity cannot be null");
        }        
        person.setName(personCreateDto.name());
        person.setAge(personCreateDto.age());
        person.setCpf(personCreateDto.cpf());

        return new PersonCreateDto(personRepository.save(person));
    }
}
